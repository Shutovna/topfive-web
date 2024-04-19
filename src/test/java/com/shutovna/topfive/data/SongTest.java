package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SongTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ItemRepository<Song> itemRepository;

    @Autowired
    private TopRepository topRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private UserRepository userRepository;

    private final String fileName = "example_file.xml";

    @Test
    public void testWithEM() {
        Song song = getSong();
        entityManager.persist(song);
        entityManager.flush();
    }

    private Song getSong() {
        Genre genre = entityManager.find(Genre.class, 1);
        Song song = new Song();
        song.setArtist("Metallica");
        song.setTitle("Unforgiven");
        song.setDescription("testDescription");
        song.setGenre(genre);
        song.setData(new ItemData(fileName, "type"));
        song.setReleasedAt(LocalDate.now());
        song.setUser(getTestUser());
        return song;
    }

    @Test
    public void testItemRepositoryCreate() {
        Genre genre = genreRepository.findByName("Metall");

        Song song = getSong();
        song = itemRepository.save(song);

        Song song2 = new Song();
        song2.setArtist("Iron Maiden");
        song2.setTitle("Number of the beast");
        song2.setDescription("testDescription2");
        song2.setGenre(genre);
        song2.setData(new ItemData(fileName, "type"));
        song2.setReleasedAt(null);
        song.setBitRate(312);
        song2.setUser(getTestUser());
        song2 = itemRepository.save(song2);

        List<Song> all = itemRepository.findAll();
        assertEquals(2, all.size());

        Song songDb = itemRepository.findById(song.getId()).orElseThrow();

        assertTrue(songDb.getId() >= 1000);
        assertEquals("Metallica", songDb.getArtist());
        assertEquals("Unforgiven", songDb.getTitle());
        assertEquals(genre, songDb.getGenre());
        assertEquals("testDescription", songDb.getDescription());
        assertEquals("example_file.xml", songDb.getData().getFilename());
        assertEquals("type", songDb.getData().getContentType());
        assertEquals(312, songDb.getBitRate());
        assertEquals(LocalDate.now(), songDb.getReleasedAt());
        assertEquals(1, songDb.getUser().getId());
        assertEquals("nikitos", songDb.getUser().getUsername());
        assertTrue(songDb.getRatings().isEmpty());

        Song songDb2 = itemRepository.findById(song2.getId()).orElseThrow();
        assertTrue(songDb2.getId() > 1000);
        assertEquals("Iron Maiden", songDb2.getArtist());
        assertEquals("Number of the beast", songDb2.getTitle());
        assertEquals(genre, songDb2.getGenre());
        assertEquals("testDescription2", songDb2.getDescription());
        assertEquals("example_file.xml", songDb2.getData().getFilename());
        assertTrue(songDb2.getRatings().isEmpty());
    }

    @Test
    public void testUpdate() {
        LocalDate releasedAt = LocalDate.now();
        Song song = getSong();
        song = itemRepository.saveAndFlush(song);

        song.setArtist("newArtist");
        song.setTitle("newTitle");
        song.setDescription("newDescription");
        song.getData().setFilename("newFilename");
        song.getData().setContentType("newFileType");
        Genre genre2 = genreRepository.findByName("Classic");
        song.setGenre(genre2);
        song.setBitRate(300);
        releasedAt = releasedAt.plusDays(1);
        song.setReleasedAt(releasedAt);

        Song songDB = itemRepository.saveAndFlush(song);
        assertEquals("newArtist", songDB.getArtist());
        assertEquals("newTitle", songDB.getTitle());
        assertEquals("newDescription", songDB.getDescription());
        assertEquals("newFilename", songDB.getData().getFilename());
        assertEquals("newFileType", songDB.getData().getContentType());
        assertEquals(genre2, songDB.getGenre());
        assertEquals(300, songDB.getBitRate());
        assertEquals(releasedAt, songDB.getReleasedAt());
        assertEquals(1, songDB.getUser().getId());
        assertEquals("nikitos", songDB.getUser().getUsername());

    }

    @Test
    public void testFailsWhenEmptySong() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            Song song = new Song();
            itemRepository.saveAndFlush(song);
        });
        String expectedMessage = "Validation failed for classes";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFailsWhenNoData() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            Genre genre = genreRepository.findByName("Metall");
            Song song = new Song();
            song.setArtist("Metallica");
            song.setTitle("Unforgiven");
            song.setDescription("testDescription");
            song.setGenre(genre);
            itemRepository.saveAndFlush(song);
        });

        String expectedMessage = "Validation failed for classes";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDelete() {
        Top top = new Top(null, TopType.SONG, "Title", "details", getTestUser());
        Song song = getSong();
        song.addTop(top);

        top = topRepository.saveAndFlush(top);
        song = itemRepository.saveAndFlush(song);

        song.removeTop(top);
        itemRepository.deleteById(song.getId());
        itemRepository.flush();

        Optional<Song> songDB = itemRepository.findById(song.getId());
        assertTrue(songDB.isEmpty());

        Optional<Top> topDB = topRepository.findById(top.getId());
        assertTrue(topDB.isPresent());
        assertTrue(topDB.get().getItems().isEmpty());
    }

    private User getTestUser() {
        return userRepository.findById(1).orElseThrow();
    }

}
