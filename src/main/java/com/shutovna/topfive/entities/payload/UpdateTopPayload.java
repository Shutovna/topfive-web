package com.shutovna.topfive.entities.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTopPayload(
        @NotBlank(message = "{ru.shutovna.msg.top.title.not_null}")
        @Size(min = 4, max = 50, message = "{ru.shutovna.msg.top.title.size}")
        String title,
        String details) {
}