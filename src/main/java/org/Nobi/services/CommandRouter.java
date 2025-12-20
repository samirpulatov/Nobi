package org.Nobi.services;

import org.Nobi.commands.CommandHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommandRouter {
    private final List<CommandHandler> handlers;

    public CommandRouter(List<CommandHandler> handlers) {
        this.handlers = handlers;
    }

    public List<BotApiMethod<?>> route(Update update) {
        String message = update.getMessage().getText();

        return handlers.stream()
                .filter(h -> h.canHandle(message))
                .findFirst()
                .map(h -> h.handle(update))
                .orElse(List.of());
    }

}
