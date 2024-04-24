package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemRow;
import com.shutovna.topfive.entities.*;
import com.shutovna.topfive.entities.payload.UpdateTopPayload;
import com.shutovna.topfive.service.TopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql({"/db/tops.sql", "/db/songs.sql"})
class TopControllerIT extends BaseTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private TopService topService;

    @Test
    void getTop_TopExists_ReturnsTopPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/1")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("tops/song_top"),
                        model().attribute("top",
                                new Top(1, TopType.SONG, "Top 1", "Details of 1", getTestUser())
                        ),
                        model().attribute("items", List.of(
                                new ItemRow<>(getTestSong2(), "/files/Fuel.mp3"),
                                new ItemRow<>(getTestSong(), "/files/Unforgiven.mp3")
                        ))
                );
    }

    @Test
    void getTop_TopDoesNotExist_ReturnsError404Page() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/123")
                .with(user(testUsername));


        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        view().name("errors/404"),
                        model().attribute("error", "Топ не найден")
                );
    }

    @Test
    void getTop_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/1")
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
    void getTopEditPage_TopExists_ReturnsTopEditPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/1")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("tops/song_top"),
                        model().attribute("top",
                                new Top(1, TopType.SONG, "Top 1", "Details of 1", getTestUser()))
                );
    }

    @Test
    void getTopEditPage_TopDoesNotExist_ReturnsError404Page() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/1234")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        view().name("errors/404"),
                        model().attribute("error", "Топ не найден")
                );
    }

    @Test
    void getTopEditPage_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/1")
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
    void updateTop_RequestIsValid_RedirectsToTopPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/1/edit")
                .param("title", "Новое название")
                .param("details", "Новое описание топа")
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

        Top top = topService.findTop(1).orElseThrow();
        assertEquals("Новое название", top.getTitle());
        assertEquals("Новое описание топа", top.getDetails());
        assertEquals(testUsername, top.getUser().getUsername());

    }

    @Test
    void updateTop_RequestIsInvalid_ReturnsTopEditPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/1/edit")
                .param("details", "  ")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        view().name("tops/edit_top"),
                        model().attribute("top",
                                new Top(1, TopType.SONG, "Top 1", "Details of 1", getTestUser())),
                        model().attribute("errors", List.of("Заголовок топа должен быть указан")),
                        model().attribute("payload", new UpdateTopPayload(null, "  "))
                );
    }

    @Test
    void updateTop_TopDoesNotExist_ReturnsError404Page() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/123/edit")
                .param("title", "New title")
                .param("details", "  ")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        view().name("errors/404"),
                        model().attribute("error", "Топ не найден")
                );
    }

    @Test
    void updateTop_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/123/edit")
                .param("title", "New title")
                .param("details", "  ")
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
    void deleteTop_TopExists_RedirectsToTopsListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/1/delete")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/tops/table")
                );

        Optional<Top> top = topService.findTop(1);
        assertTrue(top.isEmpty());
    }

    @Test
    void deleteTop_TopDoesNotExist_ReturnsError404Page() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/123/delete")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        view().name("errors/404"),
                        model().attribute("error", "Топ не найден")
                );
    }

    @Test
    void deleteTop_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/1/delete")
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