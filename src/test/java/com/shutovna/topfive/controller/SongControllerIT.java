package com.shutovna.topfive.controller;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.entities.ItemData;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import com.shutovna.topfive.service.ItemService;
import com.shutovna.topfive.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql({"/db/tops.sql", "/db/songs.sql"})
class SongControllerIT {
    private static final String filename = "example_song.mp3";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ItemService<Song, NewSongPayload, UpdateSongPayload> songService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreRepository genreRepository;

    @Value("${topfive.test.username}")
    private String testUsername;

    @Value("classpath:" + filename)
    private Resource exampleFile;

    @Test
    public void testScriptsLoaded() {
        assertEquals(2, songService.findAllItems().size());
    }

    @Test
    void getEditSongPage_ReturnsSongPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/songs/2")
                .with(user(testUsername))
                .header("Referer", "/tops/1");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attribute("song", getTestSong()),
                        model().attribute("genres", genreRepository.findAll()),
                        model().attribute("previousPage", "/tops/1"),
                        view().name("songs/edit_song")
                );
    }

    @Test
    void getEditSongPage_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/songs/2")
                .with(user(testUsername).roles());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isForbidden()
                );
    }


    @Test
    void updateSong_RequestIsValid_RedirectsToTopPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/songs/2")
                .param("title", "_changed")
                .param("artist", "Metallica_changed")
                .param("description", "Details of song_changed")
                .param("bitRate", "300")
                .param("releasedAt", "1999-11-30")
                .param("genreId", "2")
                .param("previousPage", "/songs/table")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string(HttpHeaders.LOCATION, "/songs/table")
                );

        List<Song> allSongs = songService.findAllItems();
        assertEquals(2, allSongs.size());
        Song song = songService.findItem(2).orElseThrow();
        assertEquals("_changed", song.getTitle());
        assertEquals("Metallica_changed", song.getArtist());
        assertEquals("Details of song_changed", song.getDescription());
        assertEquals(300, song.getBitRate());
        assertEquals(LocalDate.of(1999, 11, 30), song.getReleasedAt());
        assertEquals(2, song.getGenre().getId());
        assertEquals(1, song.getTops().iterator().next().getId());
        assertEquals("Unforgiven.mp3", song.getData().getFilename());
        assertEquals("audio/mpeg", song.getData().getContentType());
        assertEquals(testUsername, song.getUser().getUsername());
    }

    @Test
    void updateSong_RequestIsInValid_ReturnsEditPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/songs/2")
                .param("artist", "_")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        model().attribute("payload", new UpdateSongPayload(null, null, "_",
                                null, null, null)),
                        model().attribute("errors", Matchers.containsInAnyOrder(
                                "Название исполнителя должно быть от 2 до 50 символов",
                                "Название песни должно быть указано",
                                "Жанр должен быть указан"
                        )),
                        view().name("songs/edit_song")
                );
    }

    @Test
    void updateSong_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/songs/2")
                .param("artist", "_")
                .with(user(testUsername).roles())
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isForbidden()
                );
    }


    @Test
    void deleteSong_DeleteSong() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/songs/2/delete")
                .with(user(testUsername))
                .param("previousPage", "/songs/table")
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string(HttpHeaders.LOCATION, "/songs/table")
                );

        assertTrue(songService.findItem(2).isEmpty());
        List<Song> allSongs = songService.findAllItems();
        assertEquals(1, allSongs.size());
    }

    @Test
    void deleteSong_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/songs/2/delete")
                .with(user(testUsername).roles())
                .param("previousPage", "/songs/table")
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isForbidden()
                );

    }

    private Song getTestSong() {
        return new Song(2, "Unforgiven", "Cool song",
                new ItemData("Unforgiven.mp3", "audio/mpeg"),
                getTestUser(), "Metallica",
                LocalDate.of(1990, 11, 29),
                192, genreRepository.findById(1).orElseThrow());
    }

    private User getTestUser() {
        return userService.loadUserByUsername(testUsername);
    }
}
