package com.shutovna.topfive.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(schema = "topfive")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Song.class, name = "song"),
        @JsonSubTypes.Type(value = Video.class, name = "video"),
        @JsonSubTypes.Type(value = Photo.class, name = "photo")
})
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topfive_seq")
    protected Integer id;

    @NotNull
    protected String title;

    protected String description;

    @NotNull
    protected ItemData data;

    //@NotNull
    protected String username;

    @ManyToMany(mappedBy = "items")
    @JsonIgnore
    protected Set<Top> tops = new HashSet<>();

    @OneToMany
    @JoinTable(schema = "topfive")
    protected List<Rating> ratings = new ArrayList<>();

    public Item(Integer id, @NotNull String title, String description, @NotNull ItemData data, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.data = data;
        this.username = username;
    }

    public void addTop(Top top) {
        this.getTops().add(top);
        top.getItems().add(this);
    }

    public void removeTop(Top top) {
        this.getTops().remove(top);
        top.getItems().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
