package org.Nobi.dto;

import java.util.List;

public record ChatRequest(
        String model,
        List<Message> messages
) {
}


