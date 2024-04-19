package com.shutovna.topfive.controller;

import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql({"/db/tops.sql"})
public class SongSelectControllerIT {
    @Autowired
    private ItemRepository<Song> itemRepository;

    @Autowired
    private TopRepository topRepository;

    @Value("${topfive.test.username}")
    private String testUsername;
    @Autowired
    MockMvc mockMvc;

    @Test
    void getSongList_ReturnsSongsListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/songs/select")
                .param("topId", "1")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("songs/select_song"),
                        model().attributeExists("items")
                );
    }

    @Test
    void addSongToTop_SongAddedTopTop_ReturnsRedirectToTop() throws Exception {
        // given
        Song song = itemRepository.save(getTestSong());

        var requestBuilder = MockMvcRequestBuilders.post("/songs/select")
                .param("topId", "1")
                .param("songId", song.getId().toString())
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/tops/1")
                );

        Top top = topRepository.findById(1).orElseThrow();
        Item topSong = top.getItems().iterator().next();
        assertEquals(song, topSong);
    }

    private Song getTestSong() {
        return new Song(null, "Fuel", "Another cool song",
                new ItemData("Fuel.mp3", "audio/mpeg"),
                new User(1), "Metallica",
                LocalDate.of(1990, 1, 29),
                256, new Genre(1));
    }
}
