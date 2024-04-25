package com.shutovna.topfive.service;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.data.UserRepository;
import com.shutovna.topfive.entities.*;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import com.shutovna.topfive.util.YamlUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {
    @Mock
    TopRepository topRepository;

    @Mock
    ItemRepository<Item> itemRepository;

    @Mock
    GenreRepository genreRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    FileStorageService fileStorageService;

    @InjectMocks
    DefaultSongService songService;

    private final String testUsername = YamlUtil.getPropertyValue("topfive.test.username");

    @Test
    public void findAllSongs_ReturnsSongList() {
        // given
        Song song = new Song(1, "tille", null,
                new ItemData("file.mp3", "audio/mpeg"),
                new User(1), "artist", LocalDate.now(), 192, null);
        List<Song> songsList = List.of(song);
        doReturn(songsList).when(itemRepository).findAllByClass(Song.class);

        // when
        List<Song> result = songService.findAllSongs();

        // then
        assertEquals(songsList, result);
        verify(itemRepository).findAllByClass(Song.class);
        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(topRepository);
        verifyNoMoreInteractions(fileStorageService);
    }

    @Test
    public void findSong_ReturnsSong() {
        // given
        Song song = new Song(1, "tille", null,
                new ItemData("file.mp3", "audio/mpeg"),
                new User(1), "artist", LocalDate.now(), 192, null);
        doReturn(Optional.of(song)).when(itemRepository).findById(1);

        // when
        Optional<Song> result = songService.findItem(1);

        // then
        assertEquals(song, result.orElseThrow());
        verify(itemRepository).findById(1);
        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(topRepository);
        verifyNoMoreInteractions(fileStorageService);
    }

    @Test
    public void createSong_ReturnsCreatedSong() throws IOException {
        // given
        int topId = 1;
        int songId = 2;
        int userId = 1;
        String title = "tille";
        String description = "desc";
        String filename = "file.mp3";
        String contentType = "audio/mpeg";
        int bitRate = 192;
        String artist = "artist";
        LocalDate releasedAt = LocalDate.now();
        Integer genreId = 1;
        byte[] data = {1, 3, 4};

        Top top = new Top(topId, TopType.SONG, "title", null, new User(1, testUsername, null));
        Genre genre = new Genre(1, null, null);

        Song song = new Song(null, title, description,
                new ItemData(filename, contentType),
                new User(1, testUsername, null), artist, releasedAt, bitRate, genre);

        doReturn(Optional.of(top)).when(topRepository).findById(topId);

        doReturn(new Song(songId, title, description,
                new ItemData(filename, contentType),
                new User(1), artist, releasedAt, bitRate, genre)
        ).when(itemRepository).save(song);

        doReturn(new Genre(1, null, null)).when(genreRepository).getReferenceById(genreId);
        doReturn(new User(1, testUsername, null)).when(userRepository).getReferenceById(userId);
        MockMultipartFile file = new MockMultipartFile("file", filename, contentType, data);

        // when
        Song result = songService.createItem(new NewSongPayload(
                title, description, topId, file, artist, bitRate, releasedAt, genreId), userId);

        // then
        assertEquals(new Song(songId, title, description,
                        new ItemData(filename, contentType),
                        new User(1), artist, releasedAt, bitRate, genre),
                result);
        verify(itemRepository).save(song);
        verify(userRepository).getReferenceById(userId);
        verify(genreRepository).getReferenceById(genreId);
        verify(fileStorageService).createItemDataFile(filename, data);
        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(userRepository);
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
        ).when(itemRepository).findById(songId);

        doReturn(new Genre(genreId, null, null)).when(genreRepository).getReferenceById(1);

        // when
        songService.updateItem(songId, new UpdateSongPayload(artist, title, description, bitRate, releasedAt, genreId));

        // then
        verify(itemRepository).findById(songId);
        verify(genreRepository).getReferenceById(genreId);
        verifyNoMoreInteractions(itemRepository);
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

        doReturn(Optional.empty()).when(itemRepository).findById(songId);

        // when
        assertThrows(NoSuchElementException.class, () ->
                songService.updateItem(songId, new UpdateSongPayload(artist, title, description, bitRate, releasedAt, genreId)));

        // then
        verify(itemRepository).findById(songId);
        verifyNoMoreInteractions(itemRepository);
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
        doReturn(Optional.of(song)).when(itemRepository).findById(songId);

        //when
        this.songService.deleteItem(songId);

        //then
        verify(itemRepository).findById(songId);
        verify(itemRepository).delete(song);
        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(topRepository);
        verifyNoMoreInteractions(fileStorageService);
    }

    @Test
    public void addToTop_AddToTop() {

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

        Song song = new Song(songId, title, description,
                new ItemData(filename, contentType),
                new User(1), artist, releasedAt, bitRate, new Genre(genreId));
        doReturn(Optional.of(song)).when(itemRepository).findById(songId);

        Top top = new Top(topId, TopType.SONG, "Title %d".formatted(topId),
                "details %d".formatted(topId), new User(1));
        doReturn(Optional.of(top)).when(topRepository).findById(topId);

        //when
        this.songService.addToTop(topId, songId);

        //then
        Iterator<Item> iterator = top.getItems().iterator();
        assertEquals(song, iterator.next());
        assertFalse(iterator.hasNext());
        verify(itemRepository).findById(songId);
        verify(topRepository).findById(topId);
        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(topRepository);

    }

    @Test
    public void addToTop_NoTop_ThrowNoSuchElement() {
        // given
        int topId = 123;
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
        doReturn(Optional.of(song)).when(itemRepository).findById(songId);

        doReturn(Optional.empty()).when(topRepository).findById(topId);

        //when
        assertThrows(NoSuchElementException.class, () -> this.songService.addToTop(topId, songId));

        //then
        verify(itemRepository).findById(songId);
        verify(topRepository).findById(topId);
        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(topRepository);

    }

    @Test
    public void addToTop_NoSong_ThrowNoSuchElement() {
        // given
        int topId = 1;
        int songId = 213;

        doReturn(Optional.empty()).when(itemRepository).findById(songId);
        //when
        assertThrows(NoSuchElementException.class, () -> this.songService.addToTop(topId, songId));

        //then
        verify(itemRepository).findById(songId);
        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(topRepository);
    }
}
