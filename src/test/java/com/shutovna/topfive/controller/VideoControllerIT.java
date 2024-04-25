package com.shutovna.topfive.controller;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.entities.Video;
import com.shutovna.topfive.entities.payload.UpdateVideoPayload;
import com.shutovna.topfive.service.VideoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@Sql({"/db/tops.sql", "/db/videos.sql"})
class VideoControllerIT extends BaseTest {
    private static final String filename = "example_video.mp4";
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    VideoService videoService;

    @Test
    void getEditVideoPage_ReturnsVideoPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/videos/5")
                .with(user(testUsername))
                .header("Referer", "/tops/1");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attribute("video", getTestVideo()),
                        model().attribute("genres", genreRepository.findAllByParentId(GenreRepository.GENRE_VIDEO)),
                        model().attribute("previousPage", "/tops/1"),
                        view().name("videos/edit_video")
                );
    }

    @Test
    void getEditVideoPage_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/videos/5")
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
    void getEditVideoPage_VideoDoesNotExist_ReturnsError404Page() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/videos/222")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        view().name("errors/404"),
                        model().attribute("error", "Видео не найдено")
                );
    }


    @Test
    void updateVideo_RequestIsValid_RedirectsToTopPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/videos/5")
                .param("title", "Title1_changed")
                .param("description", "description1_changed")
                .param("genreId", "17")
                .param("place", "Place1_changed")
                .param("director", "Director1_changed")
                .param("actors", "Actors list1_changed")
                .param("previousPage", "/videos/table")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string(HttpHeaders.LOCATION, "/videos/table")
                );

        List<Video> allVideos = videoService.findAllVideos();
        assertEquals(2, allVideos.size());
        Video video = videoService.findItem(5).orElseThrow();
        assertEquals("Title1_changed", video.getTitle());
        assertEquals("description1_changed", video.getDescription());
        assertEquals("Place1_changed", video.getPlace());
        assertEquals("Director1_changed", video.getDirector());
        assertEquals("Actors list1_changed", video.getActors());
        assertEquals(17, video.getGenre().getId());
        assertTrue(video.getTops().isEmpty());
        assertEquals("Video.mp4", video.getData().getFilename());
        assertEquals("video/mpeg", video.getData().getContentType());
        assertEquals(testUsername, video.getUser().getUsername());
    }

    @Test
    void updateVideo_RequestIsInValid_ReturnsEditPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/videos/5")
                .param("title", "")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        model().attribute("payload", new UpdateVideoPayload("", null, null,
                                null, null, null, null)),
                        model().attribute("errors", Matchers.containsInAnyOrder(
                                "Название видео должно быть от 1 до 50 символов",
                                "Название видео должно быть указано",
                                "Жанр должен быть указан"
                        )),
                        view().name("videos/edit_video")
                );
    }

    @Test
    void updateVideo_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/videos/5")
                .param("title", "_")
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
    void updateVideo_VideoDoesNotExist_ReturnsError404Page() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/videos/2222")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        view().name("errors/404"),
                        model().attribute("error", "Видео не найдено")
                );
    }


    @Test
    void deleteVideo_DeleteVideo() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/videos/5/delete")
                .with(user(testUsername))
                .param("previousPage", "/videos/table")
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string(HttpHeaders.LOCATION, "/videos/table")
                );

        assertTrue(videoService.findItem(5).isEmpty());
        List<Video> allVideos = videoService.findAllVideos();
        assertEquals(1, allVideos.size());
    }

    @Test
    void deleteVideo_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/videos/5/delete")
                .with(user(testUsername).roles())
                .param("previousPage", "/videos/table")
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
