package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.Video;
import com.shutovna.topfive.entities.payload.NewVideoPayload;
import com.shutovna.topfive.entities.payload.UpdateVideoPayload;

import java.io.IOException;

public interface VideoService extends ItemService<Video> {

    Video createItem(NewVideoPayload payload, Integer userId) throws IOException;

    public void updateItem(Integer itemId, UpdateVideoPayload payload);
}
