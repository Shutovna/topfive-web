package com.shutovna.topfive.entities.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTopPayload(
        @NotBlank(message = "{ru.nikitos.msg.top.title.not_null}")
        @Size(min = 4, max = 50, message = "{ru.nikitos.msg.top.title.size}")
        String title,
        String details) {
}