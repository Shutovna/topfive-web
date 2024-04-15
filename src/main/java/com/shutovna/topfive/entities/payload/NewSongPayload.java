package com.shutovna.topfive.entities.payload;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class NewSongPayload {
    @NotBlank(message = "{ru.nikitos.msg.song.artist.not_null}")
    @Size(min = 2, max = 50, message = "{ru.nikitos.msg.song.artist.size}")
    String artist;
    @NotBlank(message = "{ru.nikitos.msg.song.title.not_null}")
    @Size(min = 1, max = 50, message = "{ru.nikitos.msg.song.title.size}")
    String title;
    String description;
    Integer bitRate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate releasedAt;

    @NotNull(message = "{ru.nikitos.msg.song.genre.not_null}")
    Integer genreId;

    Integer topId;

    String fileName;
    byte[] data;
    String type;

    @AssertTrue(message = "{ru.nikitos.msg.song.fileName.not_null}")
    public boolean isFileSet() {
        return !(StringUtils.isEmpty(fileName) || ArrayUtils.isEmpty(data));
    }

    @AssertTrue(message = "{ru.nikitos.msg.song.type.is_audio}")
    public boolean isAudioFile() {
        return !StringUtils.isEmpty(type) && type.startsWith("audio");
    }


    @Override
    public String toString() {
        return "NewSongPayload{" +
                "artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", bitRate=" + bitRate +
                ", releasedAt=" + releasedAt +
                ", genreId=" + genreId +
                ", topId=" + topId +
                ", fileName='" + fileName + '\'' +
                ", topType='" + type + '\'' +
                '}';
    }
}