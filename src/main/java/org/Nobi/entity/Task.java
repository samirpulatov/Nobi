package org.Nobi.entity;

import lombok.Data;
import org.Nobi.enums.TaskStatus;

@Data
public class Task {
    private  Integer id;
    private  Integer userId;
    private  String description;
    private  TaskStatus taskStatus;

    public Task( Integer userId, String description, TaskStatus taskStatus) {

        this.userId = userId;
        this.description = description;
        this.taskStatus = taskStatus;

    }
}
