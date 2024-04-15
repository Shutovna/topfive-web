package com.shutovna.topfive.service;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.data.SongRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.entities.*;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultSongService implements SongService {
    private final SongRepository songRepository;

    private final TopRepository topRepository;

    private final FileStorageService fileStorageService;

    private final GenreRepository genreRepository;

    @Override
    public List<Song> findAllSongs() {
        return songRepository.findAll();
    }

    @Override
    public Song createSong(NewSongPayload payload, String username) {
        ItemData itemData = new ItemData();
        itemData.setFilename(payload.getFileName());
        itemData.setContentType(payload.getType());
        fileStorageService.createItemDataFile(payload.getFileName(), payload.getData());

        Song song = new Song();
        song.setArtist(payload.getArtist());
        song.setTitle(payload.getTitle());
        song.setDescription(payload.getDescription());
        song.setBitRate(payload.getBitRate());
        song.setReleasedAt(payload.getReleasedAt());
        song.setGenre(genreRepository.findById(payload.getGenreId()).orElseThrow());
        song.setData(itemData);
        song.setUsername(username);

        if (payload.getTopId() != null) {
            song.addTop(topRepository.findById(payload.getTopId()).orElseThrow());
        }

        return songRepository.save(song);
    }

    @Override
    public Optional<Song> findSong(Integer songId) {
        return songRepository.findById(songId);
    }

    @Override
    public void updateSong(Integer songId, UpdateSongPayload payload) {
        Song song = songRepository.findById(songId).orElseThrow(NoSuchElementException::new);
        song.setArtist(payload.getArtist());
        song.setTitle(payload.getTitle());
        song.setDescription(payload.getDescription());
        song.setBitRate(payload.getBitRate());
        song.setReleasedAt(payload.getReleasedAt());
        song.setGenre(genreRepository.findById(payload.getGenreId()).orElseThrow());
    }

    @Override
    public void deleteSong(Integer songId) {
        Optional<Song> song = songRepository.findById(songId);
        if (song.isPresent()) {
            for (Top top : new ArrayList<>(song.get().getTops())) {
                song.get().removeTop(top);
            }
            songRepository.delete(song.get());
        }
    }
}
