package com.shutovna.topfive.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql({"/db/tops.sql", "/db/songs.sql"})
public class SongSelectControllerIT {
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
}
