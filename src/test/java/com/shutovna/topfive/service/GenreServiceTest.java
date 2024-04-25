package com.shutovna.topfive.service;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.entities.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {
    @Mock
    GenreRepository genreRepository;

    @InjectMocks
    DefaultGenreService genreService;

    @Test
    void findAllGenres_ReturnsGenreList() {
        List<Genre> genres = IntStream.rangeClosed(1, 4).mapToObj(
                value -> new Genre(value, "Title %d".formatted(value), null)
        ).toList();

        doReturn(genres).when(genreRepository).findAll();

        List<Genre> result = genreService.findGenres();

        assertEquals(genres, result);

        verify(this.genreRepository).findAll();
        verifyNoMoreInteractions(this.genreRepository);
    }
}