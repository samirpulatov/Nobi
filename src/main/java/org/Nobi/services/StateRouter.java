package org.Nobi.services;

import org.Nobi.enums.UserState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class StateRouter {

    private final static Logger LOGGER = LoggerFactory.getLogger(StateRouter.class);

    private final UserStateService userStateService;
    private final TaskInputHandler taskInputHandler;

    public StateRouter(UserStateService userStateService, TaskInputHandler taskInputHandler) {
        this.userStateService = userStateService;
        this.taskInputHandler = taskInputHandler;
    }

    public List<BotApiMethod<?>> route(Update update) {
        Long chatId = update.getMessage().getChatId();
        UserState userState = userStateService.getUserState(chatId);

        if(userState == UserState.WAITING_TASKS_INPUT) {
            LOGGER.info("Calling TaskInputHandler");
            return  taskInputHandler.handle(update);
        }
        return null;

    }
}
