package com.shutovna.topfive.entities.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ItemPayload {
    @NotBlank(message = "{ru.nikitos.msg.song.title.not_null}")
    @Size(min = 1, max = 50, message = "{ru.nikitos.msg.song.title.size}")
    String title;
    String description;

    public ItemPayload(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
