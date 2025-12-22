package org.Nobi.dto;

import lombok.Data;
import org.Nobi.enums.TaskStatus;

import java.time.LocalDateTime;


@Data
public class TaskDto {
    private String taskDescription;
    private LocalDateTime optionalDateTime;
    private TaskStatus taskStatus;
}
