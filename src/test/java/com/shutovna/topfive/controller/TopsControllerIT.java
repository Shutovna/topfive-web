package com.shutovna.topfive.controller;

import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.TopType;
import com.shutovna.topfive.entities.payload.NewTopPayload;
import com.shutovna.topfive.service.TopService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TopsControllerIT extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private TopService topService;

    @Test
    @Sql("/db/tops.sql")
    void getTopList_ReturnsTopsListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/table")
                .queryParam("filter", "top")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("tops/top_table"),
                        model().attribute("filter", "top"),
                        model().attribute("tops", List.of(
                                new Top(1, TopType.SONG, "Top 1", "Details of 1", getTestUser()),
                                new Top(2, TopType.VIDEO, "Top 2", "Details of 2", getTestUser()),
                                new Top(3, TopType.PHOTO, "Top 3", "Details of 3", getTestUser()),
                                new Top(4, TopType.SONG, "Top 4", "Details of 4", getTestUser())
                        ))
                );
    }

    @Test
    void getTopList_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/table")
                .queryParam("filter", "top")
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
    void getNewTopPage_ReturnsTopPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/create")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("tops/new_top")
                );
    }

    @Test
    void getNewTopPage_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/tops/create")
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
    void createTop_RequestIsValid_RedirectsToTopPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/create")
                .param("title", "New top")
                .param("topType", "SONG")
                .param("details", "Details of top 1")
                .with(user(testUsername))
                .with(csrf());


        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string(HttpHeaders.LOCATION, "/tops/1000")
                );

        Top top = topService.findTop(1000).orElseThrow();
        assertEquals("New top", top.getTitle());
        assertEquals("Details of top 1", top.getDetails());
        assertEquals(TopType.SONG, top.getType());
        assertEquals("shutovna", top.getUser().getUsername());
    }

    @Test
    void createTop_RequestIsInvalid_ReturnsNewTopPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/create")
                .param("title", "   ")
                .with(user(testUsername))
                .with(csrf());

        // when
        ModelResultMatchers model = model();
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        view().name("tops/new_top"),
                        model.attribute("payload", new NewTopPayload("   ", null, null)),
                        model.attribute("errors",
                                Matchers.containsInAnyOrder(
                                        "Заголовок топа должен быть указан",
                                        "Заголовок топа должен быть от 4 до 50 символов",
                                        "Тип топа должен быть указан")
                        )
                );
    }

    @Test
    void createTop_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/tops/create")
                .param("title", "New top")
                .param("details", "Top details")
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
