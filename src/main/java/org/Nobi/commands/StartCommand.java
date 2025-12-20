package org.Nobi.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class StartCommand extends CommandHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);

    @Override
    public boolean canHandle(String command) {
        return command.equals("/start");
    }

    public List<BotApiMethod<?>> handle(Update update) {
        LOGGER.info("Received Update {}", update);
        LOGGER.info("START command received");
        String firstName = update.getMessage().getFrom().getFirstName();
        Long chatId = update.getMessage().getChatId();
        return List.of(
                introMessage(chatId,firstName)
        );
    }

    private SendMessage introMessage(Long chatId,String firstName) {

        return SendMessage.builder()
                .chatId(chatId)
                .text(
                "Здравствуйте, " + firstName + " 👋.\n\n" +
                "Меня зовут Nobi 👽 — Ваш персональный ассистент в Telegram. " +
                "Я помогу Вам с повседневными задачами: от составления удобного расписания " +
                "до анализа погоды и рекомендаций по выбору одежды.\n\n" +
                "Чтобы узнать, на что я способен, введите команду /list."
                )
                .build();
    }
}
