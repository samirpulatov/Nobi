package org.Nobi.services;

import org.Nobi.repository.UserRepository;
import org.Nobi.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

    public boolean userExists(Long chat_id) {
        return userRepository.existsByChatId(chat_id);
    }

    public Integer getUserId(Long chat_id) {
        return userRepository.getUserID(chat_id);
    }


}
