package com.shutovna.topfive.entities.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateVideoPayload {
    @NotBlank(message = "{ru.shutovna.msg.video.title.not_null}")
    @Size(min = 1, max = 50, message = "{ru.shutovna.msg.video.title.size}")
    String title;
    String description;

    String place;

    String director;

    String actors;

    Integer releasedYear;

    @NotNull(message = "{ru.shutovna.msg.video.genre.not_null}")
    Integer genreId;

    public UpdateVideoPayload(String title, String description, String place, String director,
                              String actors, Integer releasedYear, Integer genreId) {
        this.title = title;
        this.description = description;
        this.place = place;
        this.director = director;
        this.actors = actors;
        this.releasedYear = releasedYear;
        this.genreId = genreId;
    }
}