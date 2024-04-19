package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class SongServiceIT {
    @Autowired
    ItemService<Song, NewSongPayload, UpdateSongPayload> songService;

    @Autowired
    TopService topService;

    @Test
    @Sql({"/db/tops.sql", "/db/songs.sql"})
    public void deleteSong_SongIsDeleted() {
        Song song = songService.findItem(2).orElseThrow();
        assertFalse(song.getTops().isEmpty());

        Top top = topService.findTop(1).orElseThrow();

        songService.deleteItem(2);
        songService.deleteItem(3);

        assertTrue(top.getItems().isEmpty());
    }
}
