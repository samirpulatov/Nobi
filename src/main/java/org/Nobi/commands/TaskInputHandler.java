package org.Nobi.commands;

import org.Nobi.enums.UserState;
import org.Nobi.services.OpenAiService;
import org.Nobi.services.UserStateService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TaskInputHandler {
    private final UserStateService userStateService;
    private final OpenAiService openAiService;

    public TaskInputHandler(UserStateService userStateService, OpenAiService openAiService) {
        this.userStateService = userStateService;
        this.openAiService = openAiService;
    }

    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        List<String> tasks = openAiService.parseTasks(text);
        userStateService.setUserState(chatId, UserState.WAITING_TASKS_INPUT);
        return List.of(
            waitForParsing(chatId),
            formattedTasksMessage(chatId,tasks)

        );
    }


    private SendMessage waitForParsing(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Подождите секундочку ⌛")
                .build();
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
