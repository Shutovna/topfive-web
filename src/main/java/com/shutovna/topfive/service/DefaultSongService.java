package com.shutovna.topfive.service;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.data.UserRepository;
import com.shutovna.topfive.entities.*;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultSongService implements SongService {
    private final ItemRepository<Song> itemRepository;

    private final TopRepository topRepository;

    private final FileStorageService fileStorageService;

    private final GenreRepository genreRepository;

    private final UserRepository userRepository;

    @Override
    public List<Song> findAllSongs() {
        return itemRepository.findAll();
    }

    @Override
    public Song createSong(String artist, String title, String description, Integer bitRate, LocalDate releasedAt,
                           Integer genreId, Integer topId, String filename, byte[] data, String type, Integer userId) {
        ItemData itemData = new ItemData();
        itemData.setFilename(filename);
        itemData.setContentType(type);
        fileStorageService.createItemDataFile(filename, data);

        Song song = new Song();
        song.setArtist(artist);
        song.setTitle(title);
        song.setDescription(description);
        song.setBitRate(bitRate);
        song.setReleasedAt(releasedAt);
        song.setGenre(genreRepository.getReferenceById(genreId));
        song.setData(itemData);
        song.setUser(userRepository.getReferenceById(userId));

        if (topId != null) {
            song.addTop(topRepository.findById(topId).orElseThrow());
        }

        return itemRepository.save(song);
    }

    @Override
    public Optional<Song> findSong(Integer songId) {
        return itemRepository.findById(songId);
    }

    @Override
    public void updateSong(Integer songId, UpdateSongPayload payload) {
        Song song = itemRepository.findById(songId).orElseThrow(NoSuchElementException::new);
        song.setArtist(payload.getArtist());
        song.setTitle(payload.getTitle());
        song.setDescription(payload.getDescription());
        song.setBitRate(payload.getBitRate());
        song.setReleasedAt(payload.getReleasedAt());
        song.setGenre(genreRepository.findById(payload.getGenreId()).orElseThrow());
    }

    @Override
    public void deleteSong(Integer songId) {
        Optional<Song> song = itemRepository.findById(songId);
        if (song.isPresent()) {
            for (Top top : new ArrayList<>(song.get().getTops())) {
                song.get().removeTop(top);
            }
            itemRepository.delete(song.get());
        }
    }

    @Override
    public void addToTop(Integer topId, Integer songId) {
        Song song = itemRepository.findById(songId).orElseThrow(NoSuchElementException::new);
        Top top = topRepository.findById(topId).orElseThrow(NoSuchElementException::new);
        top.addItem(song);
    }

    @Override
    public void removeFromTop(Integer topId, Integer songId) {
        Song song = itemRepository.findById(songId).orElseThrow(NoSuchElementException::new);
        Top top = topRepository.findById(topId).orElseThrow(NoSuchElementException::new);
        top.removeItem(song);
    }
}
