package com.shutovna.topfive.controller;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.entities.ItemData;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.entities.Video;
import com.shutovna.topfive.service.DefaultUserService;
import com.shutovna.topfive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public abstract class BaseTest {
    @Value("${topfive.test.username}")
    protected String testUsername;
    @Autowired
    protected UserService userService;

    @Autowired
    protected GenreRepository genreRepository;

    protected User getTestUser() {
        return userService.loadUserByUsername(testUsername);
    }

    protected Song getTestSong() {
        return new Song(2, "Unforgiven", "Cool song",
                new ItemData("Unforgiven.mp3", "audio/mpeg"),
                getTestUser(), "Metallica",
                LocalDate.of(1990, 11, 29),
                192, genreRepository.getReferenceById(12));
    }

    protected Song getTestSong2() {
        return new Song(3, "Fuel", "Another cool song",
                new ItemData("Fuel.mp3", "audio/mpeg"),
                getTestUser(), "Metallica",
                LocalDate.of(1996, 1, 29),
                256, genreRepository.getReferenceById(12));
    }

    protected Video getTestVideo() {
        return new Video(5, "Video", "Cool video",
                new ItemData("Video.mp4", "video/mpeg"),
                getTestUser(), "Place 1", "Director 1", "Actors list 1", 2000,
                genreRepository.getReferenceById(15));
    }

    protected Video getTestVideo2() {
        return new Video(6, "Video2", "Cool video2",
                new ItemData("Video2.mp4", "video/mpeg"),
                getTestUser(), "Place 2", "Director 2", "Actors list 2", 2005,
                genreRepository.getReferenceById(16));
    }
}
