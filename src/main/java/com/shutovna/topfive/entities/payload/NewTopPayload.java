package com.shutovna.topfive.entities.payload;

import com.shutovna.topfive.entities.TopType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewTopPayload(
        @NotBlank(message = "{ru.nikitos.msg.top.title.not_blank}")
        @Size(min = 4, max = 50, message = "{ru.nikitos.msg.top.title.size}")
        String title,
        String details,
        @NotNull(message = "{ru.nikitos.msg.top.type.not_null}")

        TopType topType) {

}