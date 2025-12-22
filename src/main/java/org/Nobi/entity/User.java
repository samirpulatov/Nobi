package org.Nobi.entity;

import lombok.*;
import org.Nobi.enums.UserRole;
import org.Nobi.enums.UserState;

@Data
@NoArgsConstructor
public class User {
    private Integer id;
    private  Long chat_id;
    private  String userName;
    private  String firstName;
    private  String lastName;
    private  UserRole userRole;
    private UserState userState;

    public User(Long chatId,String userName,String firstName, String lastName, UserRole userRole) {
        this.chat_id = chatId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;

    }
}
