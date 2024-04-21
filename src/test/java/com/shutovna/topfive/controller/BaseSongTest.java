package com.shutovna.topfive.controller;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.entities.ItemData;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import com.shutovna.topfive.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public abstract class BaseSongTest extends BaseTest {
    @Autowired
    ItemService<Song, NewSongPayload, UpdateSongPayload> songService;

    @Autowired
    protected GenreRepository genreRepository;

    protected Song getTestSong2() {
        return new Song(3, "Fuel", "Another cool song",
                new ItemData("Fuel.mp3", "audio/mpeg"),
                getTestUser(), "Metallica",
                LocalDate.of(1990, 1, 29),
                256, genreRepository.getReferenceById(1));
    }

    protected Song getTestSong() {
        return new Song(2, "Unforgiven", "Cool song",
                new ItemData("Unforgiven.mp3", "audio/mpeg"),
                getTestUser(), "Metallica",
                LocalDate.of(1990, 11, 29),
                192, genreRepository.getReferenceById(1));
    }
}
