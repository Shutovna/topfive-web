package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemRow;
import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.entities.Song;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql({"/db/tops.sql", "/db/songs.sql", "/db/videos.sql"})
public class ItemSelectControllerIT extends BaseSongTest {
    @Autowired
    private ItemRepository<Song> itemRepository;

    @Autowired
    private TopRepository topRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getSongList_ForFullSongTop_ReturnsEmptyListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/items/select")
                .param("topId", "1")
                .param("successUrl", "/tops/1")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("songs/select_song"),
                        model().attribute("items", List.of())
                );
    }

    @Test
    void getSongList_ForEmptySongTop_ReturnsSongsListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/items/select")
                .param("topId", "4")
                .param("successUrl", "/tops/4")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("songs/select_song"),
                        model().attribute("items", List.of(
                                new ItemRow<>(getTestSong(), "/files/Unforgiven.mp3"),
                                new ItemRow<>(getTestSong2(), "/files/Fuel.mp3")))
                );
    }

    @Test
    void getSongList_ForEmptyVideoTop_ReturnsVideosListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/items/select")
                .param("topId", "2")
                .param("successUrl", "/tops/2")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("videos/select_video"),
                        model().attribute("items", Matchers.iterableWithSize(2))
                );
    }

}
