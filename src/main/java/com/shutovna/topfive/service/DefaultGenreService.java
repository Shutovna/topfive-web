package com.shutovna.topfive.service;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.entities.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService{
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findGenres() {
        return genreRepository.findAll();
    }
}
