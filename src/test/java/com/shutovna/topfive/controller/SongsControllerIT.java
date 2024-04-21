package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemRow;
import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import com.shutovna.topfive.service.ItemService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SongsControllerIT extends BaseSongTest{
    private static final String filename = "example_song.mp3";

    @Autowired
    MockMvc mockMvc;






    @Value("classpath:" + filename)
    private Resource exampleFile;

    @Test
    @Sql({"/db/tops.sql", "/db/songs.sql"})
    void getSongList_ReturnsSongsListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/songs/table")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("songs/song_table"),
                        model().attribute("items", List.of(
                                new ItemRow<>(getTestSong(), "/files/Unforgiven.mp3"),
                                new ItemRow<>(getTestSong2(), "/files/Fuel.mp3")
                        ))
                );
    }


    @Test
    void getSongList_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/songs/table")
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
    void getNewSongPage_ReturnsSongPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/songs/create")
                .param("topId", "1")
                .with(user(testUsername))
                .header("Referer", "/tops/1");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attribute("topId", 1),
                        model().attribute("genres", genreRepository.findAll()),
                        model().attribute("previousPage", "/tops/1"),
                        view().name("songs/new_song")
                );
    }

    @Test
    void getNewSongPage_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/songs/create")
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
    @Sql("/db/tops.sql")
    void createSong_RequestIsValid_RedirectsToSongPage() throws Exception {
        // given
        byte[] content = exampleFile.getContentAsByteArray();

        var requestBuilder = MockMvcRequestBuilders.multipart("/songs/create")
                .part(new MockPart("file", "One.mp3", content, new MediaType("audio", "mpeg")))
                .param("title", "One")
                .param("artist", "Metallica")
                .param("description", "Details of song")
                .param("topId", "1")
                .param("genreId", "1")
                .param("bitRate", "192")
                .param("releasedAt", "1990-11-29")
                .param("previousPage", "/tops/1")
                .with(user(testUsername))
                .with(csrf());


        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string(HttpHeaders.LOCATION, "/tops/1")
                );

        List<Song> allSongs = songService.findAllItems();
        assertEquals(1, allSongs.size());
        Song song = allSongs.get(0);
        assertEquals("One", song.getTitle());
        assertEquals("Metallica", song.getArtist());
        assertEquals("Details of song", song.getDescription());
        assertEquals(192, song.getBitRate());
        assertEquals(LocalDate.of(1990, 11, 29), song.getReleasedAt());
        assertEquals(1, song.getGenre().getId());
        assertEquals(1, song.getTops().iterator().next().getId());
        assertEquals("One.mp3", song.getData().getFilename());
        assertEquals("audio/mpeg", song.getData().getContentType());
        assertEquals(testUsername, song.getUser().getUsername());
    }

    @Test
    @Sql("/db/tops.sql")
    void createSong_RequestIsInvalid_ReturnsNewSongPage() throws Exception {
        // given
        byte[] content = new byte[]{};
        MockPart mockPart = new MockPart("file", "One.mp3", content, new MediaType("video", "mpeg"));
        var requestBuilder = MockMvcRequestBuilders.multipart("/songs/create")
                .part(mockPart)
                .param("artist", " ")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        view().name("songs/new_song"),
                        model().attribute("payload", new NewSongPayload(
                                null, null, null,
                                new MockMultipartFile("file",
                                        "One.mp3", "video/mpeg", content),
                                " ", null, null, null
                        )),
                        model().attribute("errors",
                                Matchers.containsInAnyOrder(
                                        "Название исполнителя должно быть указано",
                                        "Название исполнителя должно быть от 2 до 50 символов",
                                        "Название песни должно быть указано",
                                        "Жанр должен быть указан",
                                        "Файл должен быть указан",
                                        "Должен быть аудиофайл")
                        )
                );
    }

    @Test
    @Sql("/db/tops.sql")
    void createSong_NoFileSet_ReturnsNewSongPage() throws Exception {
        // given
        byte[] content = new byte[]{};
        var requestBuilder = MockMvcRequestBuilders.multipart("/songs/create")
                .param("title", "One")
                .param("artist", "Metallica")
                .param("description", "Details of song")
                .param("topId", "1")
                .param("genreId", "1")
                .param("bitRate", "192")
                .param("releasedAt", "1990-11-29")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        view().name("songs/new_song")

                );
    }

    @Test
    void createSong_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/songs/create")
                .param("title", "New song")
                .param("description", "Song details")
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
}
