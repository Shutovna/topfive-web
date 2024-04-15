package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.*;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TopTest {
    @Autowired
    TopRepository topRepository;

    @Autowired
    SongRepository songRepository;

    @Value("${topfive.test.username}")
    String testUsername;

    @Test
    public void testCreate() {
        Top top = new Top(null, TopType.SONG, "newTitle", "newDetails", getTestUser());
        top = topRepository.saveAndFlush(top);

        Top topDB = topRepository.findById(top.getId()).orElseThrow();
        assertEquals("newTitle", topDB.getTitle());
        assertEquals("newDetails", topDB.getDetails());
        assertEquals(TopType.SONG, topDB.getType());
        assertTrue(topDB.getItems().isEmpty());
        assertTrue(topDB.getRatings().isEmpty());
    }

    private String getTestUser() {
        return testUsername;
    }

    @Test
    public void testUpdate() {
        Top top = new Top(null, TopType.VIDEO, "newTitle", "newDetails", getTestUser());
        top = topRepository.saveAndFlush(top);

        top.setTitle("updatedTitle");
        top.setDetails("updatedDetails");
        top.setType(TopType.PHOTO);
        topRepository.flush();

        Top topDB = topRepository.findById(top.getId()).orElseThrow();
        assertEquals("updatedTitle", topDB.getTitle());
        assertEquals("updatedDetails", topDB.getDetails());
        assertEquals(TopType.PHOTO, topDB.getType());
    }

    @Test
    public void testFailsWhenNoData() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            Top top = new Top();
            topRepository.saveAndFlush(top);
        });
        String expectedMessage = "Validation failed for classes";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFailsWhenNoType() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            Top top = new Top(null, null, "title", null, null);
            topRepository.saveAndFlush(top);
        });
        String expectedMessage = "Validation failed for classes";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddRemoveItems() {
        Top top = new Top(null, TopType.SONG, "newTitle", "newDetails", getTestUser());
        Song song = new Song(null, "title", "desc",
                new ItemData("file.txt", "newFileType"), getTestUser(),
                "artist", null, null, new Genre(1));
        top.addItem(song);
        assertEquals(1, song.getTops().size());
        assertEquals(1, top.getItems().size());

        top = topRepository.saveAndFlush(top);
        assertTrue(top.getItems().contains(song));

        top.removeItem(song);
        assertTrue(song.getTops().isEmpty());
        assertTrue(top.getItems().isEmpty());
    }

    @Test
    public void testAddRemoveWithoutContext() {
        Top top = new Top(1, TopType.SONG, null, null, null);
        Song song = new Song(2, null, null, null,
                null, null, null, null, null);

        top.addItem(song);
        top.removeItem(song);
        assertTrue(top.getItems().isEmpty());
        assertTrue(song.getTops().isEmpty());
    }
}
