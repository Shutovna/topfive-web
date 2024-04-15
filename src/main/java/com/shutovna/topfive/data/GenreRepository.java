package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Genre findByName(String metall);
}
