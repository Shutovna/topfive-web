package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SongService {
    List<Song> findAllSongs();

    Song createSong(String artist, String title, String description, Integer bitRate, LocalDate releasedAt,
                    Integer genreId, Integer topId, String filename, byte[] data, String type, Integer userId);

    Optional<Song> findSong(Integer songId);

    void updateSong(Integer songId, UpdateSongPayload payload);

    void deleteSong(Integer songId);

    void addToTop(Integer topId, Integer songId);

    void removeFromTop(Integer topId, Integer songId);
}
