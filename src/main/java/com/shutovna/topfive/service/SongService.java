package com.shutovna.topfive.service;
import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;

import java.util.List;
import java.util.Optional;

public interface SongService {
    List<Song> findAllSongs();

    Song createSong(NewSongPayload payload, User user);

    Optional<Song> findSong(Integer songId);

    void updateSong(Integer songId, UpdateSongPayload payload);

    void deleteSong(Integer songId);
}
