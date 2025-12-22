package org.Nobi.repository;

import jakarta.annotation.PostConstruct;
import org.Nobi.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void enableForeignKey() {
        jdbcTemplate.execute("PRAGMA foreign_keys=ON;");
    }


    public void createTable(){
        LOGGER.info("Creating table Tasks");
        jdbcTemplate.execute("""
        CREATE TABLE IF NOT EXISTS tasks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        userId INTEGER NOT NULL,
                        description TEXT NOT NULL,
                        taskStatus TEXT NOT NULL,
                        FOREIGN KEY(userId) REFERENCES users(id)
            );
        """);
    }

    public void saveTask(Task task){
        Integer userId = task.getUserId();
        String description = task.getDescription();
        String status = task.getTaskStatus().name();
        jdbcTemplate.update(
            """
                INSERT OR IGNORE INTO tasks (userId, description, taskStatus) 
                VALUES (?,?,?)    
                """,
                userId,
                description,
                status

        );
    }
}
