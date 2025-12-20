package org.Nobi.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class DailyTasksCommand extends CommandHandler {



    @Override
    public boolean canHandle(String command) {
        return command.equals("/daily_tasks");
    }

    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        return List.of(
                introMessage(chatId),
                askForPermission(chatId)
        );
    }




    private SendMessage introMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(
                """
                Эта функция поможет Вам следить за Вашими ежедневными делами 🗂️. Работает она таким образом:
                1. Вы вводите список задач, которые хотите выполнять каждый день или отслеживать.\s
                2. Я буду помогать Вам контролировать их выполнение, напоминать и упорядочивать.
                Так Вы точно ничего не забудете и сможете эффективнее планировать день!
                """)
                .build();

    }


    private SendMessage askForPermission(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(
                """
                Прежде чем что-либо вводить, я попрошу Вас нажать на кнопку «ДА», если Вы хотите, чтобы я присылал Вам напоминания о выполнении Ваших дел,
                или «НЕТ», если не хотите получать такие сообщения.

                Если Вы соглашаетесь, я смогу не только напоминать Вам о задачах, но и отслеживать их выполнение, а затем предоставлять наглядную статистику 📊:\s
                сколько задач выполнено, сколько ещё в процессе и какие повторяются чаще всего.\s
                Так Вы сможете легко контролировать свой день и видеть свой прогресс! 
               \s"""
                )
                .replyMarkup(InlineKeyboardMarkup
                        .builder()
                        .keyboardRow(
                                new InlineKeyboardRow(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Да")
                                                .callbackData("yes")
                                                .build(),
                                        InlineKeyboardButton.builder()
                                                .text("Нет")
                                                .callbackData("no")
                                                .build()
                                )
                        )
                        .build()
                )
                .build();
    }


}
