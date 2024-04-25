package com.shutovna.topfive.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "topfive")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    @SequenceGenerator(name = "topfive_seq", sequenceName = "topfive.common_sequence", initialValue = 1000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "topfive_seq")
    private Integer id;

    @NotNull
    private String name;

    private Integer parentId;

    public Genre(Integer id) {
        this.id = id;
    }
}
