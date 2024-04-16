package com.shutovna.topfive.service;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.data.SongRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.entities.*;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import com.shutovna.topfive.util.YamlUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {
    @Mock
    TopRepository topRepository;

    @Mock
    SongRepository songRepository;

    @Mock
    GenreRepository genreRepository;

    @Mock
    FileStorageService fileStorageService;

    @InjectMocks
    DefaultSongService songService;

    @Test
    public void findAllSongs_ReturnsSongList() {
        // given
        Song song = new Song(1, "tille", null,
                new ItemData("file.mp3", "audio/mpeg"),
                new User(1), "artist", LocalDate.now(), 192, null);
        List<Song> songsList = List.of(song);
        doReturn(songsList).when(songRepository).findAll();

        // when
        List<Song> result = songService.findAllSongs();

        // then
        assertEquals(songsList, result);
        verify(songRepository).findAll();
        verifyNoMoreInteractions(songRepository);
        verifyNoMoreInteractions(topRepository);
        verifyNoMoreInteractions(fileStorageService);
    }

    @Test
    public void findSong_ReturnsSong() {
        // given
        Song song = new Song(1, "tille", null,
                new ItemData("file.mp3", "audio/mpeg"),
                new User(1), "artist", LocalDate.now(), 192, null);
        doReturn(Optional.of(song)).when(songRepository).findById(1);

        // when
        Optional<Song> result = songService.findSong(1);

        // then
        assertEquals(song, result.orElseThrow());
        verify(songRepository).findById(1);
        verifyNoMoreInteractions(songRepository);
        verifyNoMoreInteractions(topRepository);
        verifyNoMoreInteractions(fileStorageService);
    }

    @Test
    public void createSong_ReturnsCreatedSong() {
        // given
        int topId = 1;
        int songId = 2;
        String title = "tille";
        String description = "desc";
        String filename = "file.mp3";
        String contentType = "audio/mpeg";
        int bitRate = 192;
        String artist = "artist";
        LocalDate releasedAt = LocalDate.now();
        Integer genreId = 1;
        byte[] data = {1, 3, 4};

        Top top = new Top(topId, TopType.SONG, "title", null, new User(1));
        Genre genre = new Genre(1, null);

        Song song = new Song(null, title, description,
                new ItemData(filename, contentType),
                new User(1), artist, releasedAt, bitRate, genre);

        doReturn(Optional.of(top)).when(topRepository).findById(topId);

        doReturn(new Song(songId, title, description,
                new ItemData(filename, contentType),
                new User(1), artist, releasedAt, bitRate, genre)
        ).when(songRepository).save(song);

        doReturn(Optional.of(new Genre(1, "Metall"))).when(genreRepository).findById(1);

        // when
        Song result = songService.createSong(
                new NewSongPayload(artist, title, description, bitRate, releasedAt, genreId,
                        topId, filename, data, contentType),
                new User(1));

        // then
        assertEquals(new Song(songId, title, description,
                        new ItemData(filename, contentType),
                        new User(1), artist, releasedAt, bitRate, genre),
                result);
        verify(songRepository).save(song);
        verify(genreRepository).findById(genreId);
        verify(fileStorageService).createItemDataFile(filename, data);
        verifyNoMoreInteractions(songRepository);
        verifyNoMoreInteractions(topRepository);
        verifyNoMoreInteractions(genreRepository);
        verifyNoMoreInteractions(fileStorageService);
    }

    @Test
    public void UpdateSong_SongExists_UpdateSong() {
        // given
        int songId = 2;
        String title = "tille";
        String description = "desc";
        String filename = "file.mp3";
        String contentType = "audio/mpeg";
        int bitRate = 192;
        String artist = "artist";
        LocalDate releasedAt = LocalDate.now();
        Integer genreId = 1;

        doReturn(Optional.of(new Song(songId, title, description,
                new ItemData(filename, contentType),
                new User(1), artist, releasedAt, bitRate, new Genre(genreId)))
        ).when(songRepository).findById(songId);

        doReturn(Optional.of(new Genre(genreId, "Metall"))).when(genreRepository).findById(1);

        // when
        songService.updateSong(songId, new UpdateSongPayload(artist, title, description, bitRate, releasedAt, genreId));

        // then
        verify(songRepository).findById(songId);
        verify(genreRepository).findById(genreId);
        verifyNoMoreInteractions(songRepository);
        verifyNoMoreInteractions(topRepository);
        verifyNoMoreInteractions(genreRepository);
        verifyNoMoreInteractions(fileStorageService);
    }

    @Test
    public void UpdateSong_SongDoesNotExist_ThrowNoSuchElement() {
        // given
        int songId = 1234;
        String title = "tille";
        String description = "desc";
        int bitRate = 192;
        String artist = "artist";
        LocalDate releasedAt = LocalDate.now();
        Integer genreId = 1;

        doReturn(Optional.empty()).when(songRepository).findById(songId);

        // when
        assertThrows(NoSuchElementException.class, () ->
                songService.updateSong(songId, new UpdateSongPayload(artist, title, description, bitRate, releasedAt, genreId)));

        // then
        verify(songRepository).findById(songId);
        verifyNoMoreInteractions(songRepository);
        verifyNoMoreInteractions(topRepository);
        verifyNoMoreInteractions(fileStorageService);
    }

    @Test
    public void deleteSong_DeleteSong() {
        // given
        int songId = 2;
        String title = "tille";
        String description = "desc";
        String filename = "file.mp3";
        String contentType = "audio/mpeg";
        int bitRate = 192;
        String artist = "artist";
        LocalDate releasedAt = LocalDate.now();
        Integer genreId = 1;

        Song song = new Song(songId, title, description,
                new ItemData(filename, contentType),
                new User(1), artist, releasedAt, bitRate, new Genre(genreId));
        doReturn(Optional.of(song)).when(songRepository).findById(songId);

        //when
        this.songService.deleteSong(songId);

        //then
        verify(songRepository).findById(songId);
        verify(songRepository).delete(song);
        verifyNoMoreInteractions(songRepository);
        verifyNoMoreInteractions(topRepository);
        verifyNoMoreInteractions(fileStorageService);
    }

}
