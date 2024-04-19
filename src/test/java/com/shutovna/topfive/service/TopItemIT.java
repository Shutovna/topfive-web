package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.TopType;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TopItemIT {
    @Autowired
    ItemService<Song, NewSongPayload, UpdateSongPayload> songService;
    @Autowired
    private TopService topService;

    @Test
    public void testCreateTwoLinkedEntities() throws IOException {
        Top top = topService.createTop(TopType.SONG, "newTop", "details", new User(1));

        byte[] base64Content = Base64.getEncoder().encode("example_string".getBytes());
        MockMultipartFile file = new MockMultipartFile("file", "song.mp3", "audio/mpeg", base64Content);
        Song song = songService.createItem(new NewSongPayload(
                "Arise", "song desc", top.getId(), file,
                "Sepultura", 192, LocalDate.now(), 1), 1);
    }
}
