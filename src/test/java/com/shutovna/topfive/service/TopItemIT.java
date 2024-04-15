package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.TopType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Base64;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TopItemIT {
    @Autowired
    private SongService songService;
    @Autowired
    private TopService topService;

    @Value("${topfive.test.username}")
    private String testUsername;

    @Test
    public void testCreateTwoLinkedEntities() {
        Top top = topService.createTop(TopType.SONG,"newTop", "details",testUsername);

        byte[] base64Content = Base64.getEncoder().encode("example_string".getBytes());
        Song song = songService.createSong(
                new NewSongPayload("Sepultura", "Arise", "song desc",
                        192, LocalDate.now(), 1, top.getId(), "song.mp3",
                        base64Content, "audio/mpeg"),
                testUsername);
    }
}
