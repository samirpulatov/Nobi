package org.Nobi.commands;

import org.Nobi.enums.UserState;
import org.Nobi.services.UserStateService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TaskInputHandler {
    private final UserStateService userStateService;

    public TaskInputHandler(UserStateService userStateService) {
        this.userStateService = userStateService;
    }

    public BotApiMethod<?> handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        List<String> tasks = parseTasks(text);
        userStateService.setUserState(chatId, UserState.WAITING_TASKS_INPUT);
        return formattedTasksMessage(chatId, tasks);

    }

    private List<String> parseTasks(String text) {
        return Arrays.stream(text.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    private SendMessage formattedTasksMessage(Long chatId, List<String> tasks) {
        String body = IntStream.range(0, tasks.size())
                .mapToObj(i ->(i+1) +". "+tasks.get(i))
                .collect(Collectors.joining("\n"));
        userStateService.setUserState(chatId, UserState.IDLE);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Отлично! Ваши ежедневные задачи:\n\n" + body)
                .build();
    }
}
