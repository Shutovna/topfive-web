package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Genre;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GenreTest {
    @Autowired
    GenreRepository genreRepository;

    @Test
    public void testFindAll() {
        List<Genre> all = genreRepository.findAll();
        assertEquals(10, all.size());
    }

    @Test
    public void testFindMusicGenres() {
        List<Genre> all = genreRepository.findAllByParentId(GenreRepository.GENRE_MUSIC);
        assertEquals(4, all.size());
        for(Genre g : all) {
            assertEquals(GenreRepository.GENRE_MUSIC, g.getParentId());
        }
    }

    @Test
    public void testFindVideoGenres() {
        List<Genre> all = genreRepository.findAllByParentId(GenreRepository.GENRE_VIDEO);
        assertEquals(4, all.size());
        for(Genre g : all) {
            assertEquals(GenreRepository.GENRE_VIDEO, g.getParentId());
        }
    }


    @Test
    public void testCreate() {
        Genre genre = new Genre(null, "New genre", null);
        Genre genreDB = genreRepository.saveAndFlush(genre);
        assertEquals("New genre", genreDB.getName());
        assertNotNull(genreDB.getId());
        assertTrue(genreDB.getId() >= 1000);
    }

    @Test
    public void testUpdate() {
        Genre genre = new Genre(null, "New genre", null);
        Genre genreDB = genreRepository.saveAndFlush(genre);
        genreDB.setName("genre2");
        genreDB = genreRepository.saveAndFlush(genre);
        assertEquals("genre2", genreDB.getName());
    }

    @Test
    public void testCreateEmptyFail() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            genreRepository.saveAndFlush(new Genre());
        });
        String expectedMessage = "Validation failed for classes";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
