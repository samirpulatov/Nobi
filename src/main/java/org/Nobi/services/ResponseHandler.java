package org.Nobi.services;

import org.Nobi.commands.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service

public class ResponseHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResponseHandler.class);

    private final UserInitializer userInitializer;
    private final StateRouter stateRouter;
    private final CommandRouter commandRouter;
    private static final List<CommandHandler> commandHandlers = new ArrayList<>();
    private final MessageSender messageSender;


    public ResponseHandler(UserInitializer userInitializer, StateRouter stateRouter, CommandRouter commandRouter, MessageSender messageSender){
        this.userInitializer = userInitializer;
        this.stateRouter = stateRouter;
        this.commandRouter = commandRouter;
        this.messageSender = messageSender;

    }



    static {
        commandHandlers.add(new StartCommand());
        commandHandlers.add(new DailyTasksCommand());
        commandHandlers.add(new ListCommand());
    }

    public void handleResponse(Update update)  {
        LOGGER.info("Received Update {}", update);
        LOGGER.info("Handle Response {}", update);

        userInitializer.initIfNeeded(update);

        List<BotApiMethod<?>> stateResponse = stateRouter.route(update);
        if (stateResponse != null) {
            stateResponse.forEach(messageSender::execute);
            return;
        }

        commandRouter.route(update)
                .forEach(messageSender::execute);
    }
}
