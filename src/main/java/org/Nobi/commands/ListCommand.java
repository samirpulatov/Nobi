package org.Nobi.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class ListCommand extends CommandHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ListCommand.class);

    @Override
    public boolean canHandle(String command) {
        return command.equals("/list");
    }

    public List<BotApiMethod<?>> handle(Update update) {
        LOGGER.info("Received Update {}", update);
        LOGGER.info("LIST command received");
        Long chatId = update.getMessage().getChatId();
         return List.of(
                 introMessage(chatId)
         );

    }

    private SendMessage introMessage(Long chatId){
        return SendMessage.builder()
                .chatId(chatId)
                .text(
                """
                Вот список команд, которые я могу выполнить для Вас:
                  
                1. /start — запустить бота и получить приветствие.
                2. /daily_tasks — помочь отслеживать ваши ежедневные дела.
                3. /list — показать список команд.
                  
                Я всегда готов помочь и сделать Ваш день немного проще! 🚀
                """
                )
                .build();
    }
}
