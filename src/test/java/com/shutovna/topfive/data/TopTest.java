package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.*;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TopTest {
    @Autowired
    TopRepository topRepository;

    @Autowired
    ItemRepository<Item> itemRepository;

    @Autowired
    private UserRepository userRepository;

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

    private User getTestUser() {
        return userRepository.findById(1).orElseThrow();
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
        Song song = itemRepository.save(new Song(null, "title", "desc",
                new ItemData("file.txt", "newFileType"), getTestUser(),
                "artist", null, null, new Genre(1)));
        Song song2 = itemRepository.save(getTestSong());
        top.addItem(song);
        top.addItem(song2);
        assertEquals(1, song.getTops().size());
        assertEquals(2, top.getItems().size());

        top = topRepository.saveAndFlush(top);
        assertTrue(top.getItems().contains(song));

        top.removeItem(song);
        top.removeItem(song2);
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

    @Test
    public void testMax5ItemsInTop() {
        Top top = new Top(null, TopType.SONG, "newTitle", "newDetails", getTestUser());
        for (int i = 0; i < 5; i++) {
            Song testSong = getTestSong();
            testSong.setTitle(testSong.getTitle() + i);
            top.addItem(itemRepository.save(testSong));
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Top>> violations = validator.validate(top);
        assertTrue(violations.isEmpty());

        top = topRepository.saveAndFlush(top);

        Song song = getTestSong();
        song.setTitle(song.getTitle() +"6");
        top.addItem(itemRepository.save(song));
        violations = validator.validate(top);
        assertEquals("размер должен находиться в диапазоне от 0 до 5", violations.iterator().next().getMessage());
    }

    private Song getTestSong() {
        return new Song(null, "Unforgiven", "Cool song",
                new ItemData("Unforgiven.mp3", "audio/mpeg"),
                getTestUser(), "Metallica",
                LocalDate.of(1990, 11, 29),
                192, new Genre(1));
    }
}
