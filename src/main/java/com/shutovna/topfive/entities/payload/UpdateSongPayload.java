package com.shutovna.topfive.entities.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class UpdateSongPayload {
    @NotBlank(message = "{ru.nikitos.msg.song.title.not_null}")
    @Size(min = 1, max = 50, message = "{ru.nikitos.msg.song.title.size}")
    String title;
    String description;

    @NotBlank(message = "{ru.nikitos.msg.song.artist.not_null}")
    @Size(min = 2, max = 50, message = "{ru.nikitos.msg.song.artist.size}")
    String artist;
    Integer bitRate;
    LocalDate releasedAt;
    @NotNull(message = "{ru.nikitos.msg.song.genre.not_null}")
    Integer genreId;

    public UpdateSongPayload(String title, String description, String artist, Integer bitRate, LocalDate releasedAt, Integer genreId) {
        this.title = title;
        this.description = description;
        this.artist = artist;
        this.bitRate = bitRate;
        this.releasedAt = releasedAt;
        this.genreId = genreId;
    }

    @Override
    public String toString() {
        return "UpdateSongPayload{" +
                "artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", bitRate=" + bitRate +
                ", releasedAt=" + releasedAt +
                ", genreId=" + genreId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateSongPayload)) return false;
        UpdateSongPayload that = (UpdateSongPayload) o;
        return Objects.equals(artist, that.artist) && Objects.equals(title, that.title)
                && Objects.equals(description, that.description) && Objects.equals(bitRate, that.bitRate)
                && Objects.equals(releasedAt, that.releasedAt) && Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artist, title, description, bitRate, releasedAt, genreId);
    }

}