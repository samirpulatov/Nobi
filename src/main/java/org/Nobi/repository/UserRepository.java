package org.Nobi.repository;

import org.Nobi.entity.User;
import org.Nobi.enums.UserRole;
import org.Nobi.enums.UserState;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs,rowNum) -> {
       var user = new User();
       user.setId(rs.getInt("id"));
       return user;
    };


    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable() {
        LOGGER.info("Creating table User");
        jdbcTemplate.execute("""
        CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        chat_id LONG UNIQUE NOT NULL,
                        userName TEXT,
                        firstName TEXT,
                        lastName TEXT,
                        userRole TEXT NOT NULL,
                        userState TEXT NOT NULL DEFAULT 'IDLE'
            );
        """);
    }

    public void saveUser(User user) {
        LOGGER.info("Saving User {}", user);
        System.out.println("Saving user: " + user.toString());
        Long chat_id = user.getChat_id();
        String userName = user.getUserName();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        UserRole userRole = user.getUserRole();
        jdbcTemplate.update(
        """
            INSERT OR IGNORE INTO users(chat_id, userName, firstName, lastName, userRole)
            VALUES (?, ?, ?, ?, ?)
            """,
            chat_id,
            userName,
            firstName,
            lastName,
            userRole.name()
        );
    }

    public @Nullable Integer getUserID(Long chat_id) {
        LOGGER.info("Retrieving User {}", chat_id);
        String sql =
                """
                SELECT id FROM users WHERE chat_id = ?;
                """;
        return jdbcTemplate.queryForObject(sql,Integer.class,chat_id);
    }

    public UserState getUserState(Long chat_id) {
        return UserState.valueOf(
                jdbcTemplate.queryForObject(
                "SELECT userState FROM users WHERE chat_id = ?",
                String.class,
                chat_id
        ));
    }

    public void updateUserState(Long chat_id, UserState userState) {
        jdbcTemplate.update(
            """
                UPDATE users SET userState = ? WHERE chat_id = ?
               """,
                userState.name(),
                chat_id
        );
    }

    public boolean existsByChatId(Long chatId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE chat_id = ?",
                Integer.class,
                chatId
        );
        return count != null && count > 0;
    }



}
