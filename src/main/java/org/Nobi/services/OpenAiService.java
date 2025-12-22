package org.Nobi.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Nobi.dto.*;
import org.Nobi.enums.TaskStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import org.springframework.http.HttpHeaders;


import java.util.Arrays;
import java.util.List;


@Service
public class OpenAiService {

    private final WebClient webClient;
    private final String model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAiService(
            @Value("${openai.base-url}") String baseurl,
            @Value("${openai.api-key}") String apikey,
            @Value("${openai.model}") String model
    ) {
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl(baseurl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apikey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public List<TaskDto> parseTasks(String userMessage) {

        String prompt =
                """
                Ты — парсер задач.
                Твоя задача: проанализировать текст пользователя и выделить отдельные задачи.     
                Правила:
                - НЕ добавляй ничего от себя
                - НЕ комментируй
                - НЕ объясняй
                - Верни ТОЛЬКО JSON
                        
                Формат ответа:
                {
                    "tasks": [
                            "string"
                    ]
                }
                        
                Если задач нет — верни пустой массив.    
                """;


        ChatRequest request = new ChatRequest(
                model,
                List.of(
                        new Message("system",prompt),
                        new Message("user",userMessage)
                )
        );

        ChatResponse response = webClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        String json_response =  response.choices().get(0).message().content();

        try {
            TaskParsingResult result =
                    objectMapper.readValue(json_response, TaskParsingResult.class);
            return result.tasks().stream()
                    .map(text -> {
                        TaskDto taskDto = new TaskDto();
                        taskDto.setTaskDescription(text);
                        taskDto.setTaskStatus(TaskStatus.IN_PROGRESS);
                        return taskDto;
                    })
                    .toList();
        } catch (JsonProcessingException e) {
            return fallbackParse(userMessage);
        }

    }


    private List<TaskDto> fallbackParse(String text) {
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> {
                    TaskDto taskDto = new TaskDto();
                    taskDto.setTaskDescription(s);
                    taskDto.setTaskStatus(TaskStatus.IN_PROGRESS);
                    return taskDto;
                })
                .toList();
    }
}
