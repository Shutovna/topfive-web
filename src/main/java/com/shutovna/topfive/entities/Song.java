package com.shutovna.topfive.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(schema = "topfive")
@Getter
@Setter
@NoArgsConstructor
public class Song extends Item {
    @NotNull
    protected String artist;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    protected LocalDate releasedAt;

    protected Integer bitRate;

    @ManyToOne
    @NotNull
    protected Genre genre;

    public Song(Integer id, String title, String description, ItemData data, String username,
                String artist, LocalDate releasedAt, Integer bitRate, Genre genre) {
        super(id, title, description, data, username);
        this.artist = artist;
        this.releasedAt = releasedAt;
        this.bitRate = bitRate;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Song{" +
                "artist='" + artist + '\'' +
                ", releasedAt=" + releasedAt +
                ", bitRate=" + bitRate +
                ", genre=" + genre +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                '}';
    }


}
