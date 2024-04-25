package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.Video;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;

import java.io.IOException;
import java.util.List;

public interface SongService extends ItemService<Song> {
    List<Song> findAllSongs();

    Song createItem(NewSongPayload payload, Integer userId) throws IOException;

    public void updateItem(Integer itemId, UpdateSongPayload payload);
}
