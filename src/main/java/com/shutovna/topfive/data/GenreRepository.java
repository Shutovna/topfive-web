package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    int GENRE_MUSIC = 1;
    int GENRE_VIDEO = 2;

    Genre findByName(String name);

    List<Genre> findAllByParentId(Integer parentId);
}
