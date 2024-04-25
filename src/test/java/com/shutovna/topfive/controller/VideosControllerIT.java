package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemRow;
import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.entities.ItemData;
import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.entities.Video;
import com.shutovna.topfive.entities.payload.NewVideoPayload;
import com.shutovna.topfive.service.VideoService;
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
class VideosControllerIT extends BaseTest {
    private static final String filename = "example_video.mp4";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    VideoService videoService;

    @Value("classpath:" + filename)
    private Resource exampleFile;

    @Test
    @Sql({"/db/tops.sql", "/db/videos.sql"})
    void getVideoList_ReturnsVideosListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/videos/table")
                .with(user(testUsername));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("videos/video_table"),
                        model().attribute("items", List.of(
                                new ItemRow<>(getTestVideo2(), "/files/Video2.mp4"),
                                new ItemRow<>(getTestVideo(), "/files/Video.mp4")
                        ))
                );
    }

    private Video getTestVideo() {
        return new Video(5, "Video", "Cool video",
                new ItemData("Video.mp4", "video/mpeg"),
                getTestUser(), "Place 1", "Director 1", "Actors list 1", 2000,
                genreRepository.getReferenceById(15));
    }

    private Video getTestVideo2() {
        return new Video(6, "Video2", "Cool video2",
                new ItemData("Video2.mp4", "video/mpeg"),
                getTestUser(), "Place 2", "Director 2", "Actors list 2", 2005,
                genreRepository.getReferenceById(16));
    }


    @Test
    void getVideoList_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/videos/table")
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
    void getNewVideoPage_ReturnsVideoPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/videos/create")
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
                        model().attribute("genres", genreRepository.findAllByParentId(GenreRepository.GENRE_VIDEO)),
                        model().attribute("previousPage", "/tops/1"),
                        view().name("videos/new_video")
                );
    }

    @Test
    void getNewVideoPage_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/videos/create")
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
    void createVideo_RequestIsValid_RedirectsToTopsPage() throws Exception {
        // given
        byte[] content = exampleFile.getContentAsByteArray();

        var requestBuilder = MockMvcRequestBuilders.multipart("/videos/create")
                .part(new MockPart("file", "Video.mp4", content, new MediaType("video", "mpeg")))
                .param("title", "Title1")
                .param("description", "description1")
                .param("topId", "2")
                .param("genreId", "15")
                .param("place", "Place1")
                .param("director", "Director1")
                .param("actors", "Actors list1")
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

        List<Video> allVideos = videoService.findAllVideos();
        assertEquals(1, allVideos.size());
        Video video = allVideos.get(0);
        assertEquals("Title1", video.getTitle());
        assertEquals("description1", video.getDescription());
        assertEquals("Place1", video.getPlace());
        assertEquals("Director1", video.getDirector());
        assertEquals("Actors list1", video.getActors());
        assertEquals(15, video.getGenre().getId());
        assertEquals(2, video.getTops().iterator().next().getId());
        assertEquals("Video.mp4", video.getData().getFilename());
        assertEquals("video/mpeg", video.getData().getContentType());
        assertEquals(testUsername, video.getUser().getUsername());
    }

    @Test
    @Sql("/db/tops.sql")
    void createVideo_RequestIsInvalid_ReturnsNewVideoPage() throws Exception {
        // given
        byte[] content = exampleFile.getContentAsByteArray();
        MockPart mockPart = new MockPart("file", "Video.mp4", content, new MediaType("video", "mpeg"));
        var requestBuilder = MockMvcRequestBuilders.multipart("/videos/create")
                .part(mockPart)
                .param("title", "")
                .with(user(testUsername))
                .with(csrf());

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        view().name("videos/new_video"),
                        model().attribute("payload", new NewVideoPayload(
                                "", null, null,
                                new MockMultipartFile("file",
                                        "Video.mp4", "video/mpeg", content),
                                null, null, null, null, null
                        )),
                        model().attribute("errors",
                                Matchers.containsInAnyOrder(
                                        "Название видео должно быть указано",
                                        "Жанр должен быть указан",
                                        "Название видео должно быть от 1 до 50 символов")
                        )
                );
    }

    @Test
    @Sql("/db/tops.sql")
    void createVideo_FileNotSet_RedirectsToVideoPage() throws Exception {
        // given
        byte[] content = new byte[]{};

        var requestBuilder = MockMvcRequestBuilders.multipart("/videos/create")
                .part(new MockPart("file", "", content, new MediaType("audio", "mpeg")))
                .param("title", "Title1")
                .param("description", "description1")
                .param("topId", "2")
                .param("genreId", "15")
                .param("place", "Place1")
                .param("director", "Director1")
                .param("actors", "Actors list1")
                .param("previousPage", "/tops/1")
                .with(user(testUsername))
                .with(csrf());


        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        view().name("videos/new_video"),
                        model().attribute("errors",
                                Matchers.containsInAnyOrder(
                                        "Файл должен быть указан",
                                        "Должен быть видеофайл")
                        )
                );
    }


    @Test
    void createVideo_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/videos/create")
                .param("title", "New video")
                .param("description", "Video details")
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
