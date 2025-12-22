package org.Nobi.services;

import org.Nobi.dto.TaskDto;
import org.Nobi.entity.Task;
import org.Nobi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public void saveDailyTasks(Long chat_id, List<TaskDto> tasksDtos) {
        Integer userId = userService.getUserId(chat_id);

        List<Task> tasks =  tasksDtos.stream()
                .map(dto -> new Task(
                        userId,
                        dto.getTaskDescription(),
                        dto.getTaskStatus()
                ))
                .toList();

        tasks.forEach(taskRepository::saveTask);
    }
}
