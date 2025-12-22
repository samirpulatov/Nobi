package org.Nobi.services;

import org.Nobi.dto.TaskDto;
import org.Nobi.entity.Task;
import org.Nobi.enums.UserState;
import org.Nobi.repository.TaskRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskInputHandler {
    private final UserStateService userStateService;
    private final TaskService taskService;
    private final OpenAiService openAiService;

    public TaskInputHandler(UserStateService userStateService, TaskRepository taskRepository, TaskService taskService, OpenAiService openAiService) {
        this.userStateService = userStateService;
        this.taskService = taskService;
        this.openAiService = openAiService;
    }

    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        List<TaskDto> tasks = openAiService.parseTasks(text);


        taskService.saveDailyTasks(chatId, tasks);



        userStateService.setUserState(chatId, UserState.WAITING_TASKS_INPUT);
        return List.of(
                waitForParsing(chatId),
                formattedTasksMessage(chatId, tasks)

        );
    }


    private SendMessage waitForParsing(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Подождите секундочку ⌛")
                .build();
    }


    private SendMessage formattedTasksMessage(Long chatId, List<TaskDto> tasks) {
        String body = tasks.stream()
                .map(this::formatCurrentTask)
                .collect(Collectors.joining("\n"));
        userStateService.setUserState(chatId, UserState.IDLE);


        return SendMessage.builder()
                .chatId(chatId)
                .text("Отлично! Ваши ежедневные задачи:\n\n" + body)
                .build();
    }


    private String formatCurrentTask(TaskDto task) {
        return switch (task.getTaskStatus()) {
            case COMPLETED -> task.getTaskDescription() + " ✅";
            case IN_PROGRESS -> task.getTaskDescription() + " 🕒";
            case FAILED -> task.getTaskDescription() + " ❌";
            default -> throw new IllegalStateException(
                    "Unknown task status: " + task.getTaskStatus()
            );
        };
    }
}
