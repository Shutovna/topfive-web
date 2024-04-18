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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

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

    MultipartFile file;

    @AssertTrue(message = "{ru.nikitos.msg.song.file.not_null}")
    public boolean isFileSet() {
        try {
            return file != null &&
                    !(StringUtils.isEmpty(file.getOriginalFilename()) || ArrayUtils.isEmpty(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AssertTrue(message = "{ru.nikitos.msg.song.type.is_audio}")
    public boolean isAudioFile() {
        if (file == null) {
            return false;
        }
        String contentType = file.getContentType();
        return !StringUtils.isEmpty(contentType) && contentType.startsWith("audio");
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
                ", file=" + file +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewSongPayload)) return false;
        NewSongPayload that = (NewSongPayload) o;
        return Objects.equals(artist, that.artist) && Objects.equals(title, that.title)
                && Objects.equals(description, that.description) && Objects.equals(bitRate, that.bitRate)
                && Objects.equals(releasedAt, that.releasedAt) && Objects.equals(genreId, that.genreId)
                && Objects.equals(topId, that.topId)
                && Objects.equals(file.getOriginalFilename(), that.getFile().getOriginalFilename());
    }

    @Override
    public int hashCode() {
        return Objects.hash(artist, title, description, bitRate, releasedAt, genreId, topId, file.getOriginalFilename());
    }
}