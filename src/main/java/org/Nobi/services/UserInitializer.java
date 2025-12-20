package org.Nobi.services;

import org.Nobi.dto.User;
import org.Nobi.enums.UserRole;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UserInitializer {
    private final UserService userService;

    public UserInitializer(UserService userService) {
        this.userService = userService;
    }

    public void initIfNeeded(Update update) {
        Long chatId =  update.getMessage().getChatId();

        if(!userService.userExists(chatId)){
            var from = update.getMessage().getFrom();
            userService.saveUser(
                    new User(
                            chatId,
                            from.getUserName(),
                            from.getFirstName(),
                            from.getLastName(),
                            UserRole.USER
                    )
            );
        }
    }
}
