package com.shutovna.topfive.entities.payload;

import com.shutovna.topfive.entities.TopType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewTopPayload(
        @NotBlank(message = "{ru.shutovna.msg.top.title.not_blank}")
        @Size(min = 4, max = 50, message = "{ru.shutovna.msg.top.title.size}")
        String title,
        String details,
        @NotNull(message = "{ru.shutovna.msg.top.type.not_null}")

        TopType topType) {

}