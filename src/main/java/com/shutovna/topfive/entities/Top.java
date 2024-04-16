package com.shutovna.topfive.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(schema = "topfive")
@Getter
@Setter
@NoArgsConstructor
public class Top {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topfive_seq")
    private Integer id;

    @NotNull
    private TopType type;

    @NotNull
    private String title;

    private String details;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(schema = "topfive", name = "top_items",
            joinColumns = @JoinColumn(name = "top_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> items = new HashSet<>();


    @ManyToMany
    @JoinTable(schema = "topfive")
    @JsonIgnore
    protected List<Rating> ratings = new ArrayList<>();

    public Top(Integer id, TopType type, String title, String details, User user) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.details = details;
        this.user = user;
    }

    public void addItem(Item item) {
        this.getItems().add(item);
        item.getTops().add(this);
    }


    public void removeItem(Item item) {
        this.getItems().remove(item);
        item.getTops().remove(this);
    }


    @Override
    public String toString() {
        return "Top{" +
                "id=" + id +
                ", topType=" + type +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", user='" + user.getUsername() + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Top top)) return false;
        return Objects.equals(getId(), top.getId()) && getType() == top.getType();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
