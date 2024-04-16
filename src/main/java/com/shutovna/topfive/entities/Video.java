package com.shutovna.topfive.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "topfive")
@Getter
@Setter
@NoArgsConstructor
public class Video extends Item {
    protected String director;

    protected String actors;

    protected Integer releasedYear;

    @ManyToOne
    protected Genre genre;

    public Video(Integer id, String title, String description, ItemData data, User user,
                 String director, String actors, Integer releasedYear, Genre genre) {
        super(id, title, description, data, user);
        this.director = director;
        this.actors = actors;
        this.releasedYear = releasedYear;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Video{" +
                "director='" + director + '\'' +
                ", actors='" + actors + '\'' +
                ", releasedYear=" + releasedYear +
                ", genre=" + genre +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", username='" + getUser().getUsername() + '\'' +
                ", data=" + data +
                '}';
    }
}
