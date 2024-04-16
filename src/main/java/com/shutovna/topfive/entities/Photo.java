package com.shutovna.topfive.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "topfive")
@Getter
@Setter
@NoArgsConstructor
public class Photo extends Item {
    protected String modelName;

    public Photo(Integer id, String title, String description, ItemData data, User user,
                 String modelName) {
        super(id, title, description, data, user);
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "modelName='" + modelName + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", username='" + getUser().getUsername() + '\'' +
                ", data=" + data +
                '}';
    }
}
