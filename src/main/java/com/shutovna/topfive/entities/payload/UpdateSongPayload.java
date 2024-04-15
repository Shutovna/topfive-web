package com.shutovna.topfive.entities.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateSongPayload {
    @NotBlank(message = "{ru.nikitos.msg.song.artist.not_null}")
    @Size(min = 2, max = 50, message = "{ru.nikitos.msg.song.artist.size}")
    String artist;
    @NotBlank(message = "{ru.nikitos.msg.song.title.not_null}")
    @Size(min = 1, max = 50, message = "{ru.nikitos.msg.song.title.size}")
    String title;
    String description;
    Integer bitRate;
    LocalDate releasedAt;
    @NotNull(message = "{ru.nikitos.msg.song.genre.not_null}")
    Integer genreId;
}