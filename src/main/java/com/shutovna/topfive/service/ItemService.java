package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.entities.payload.NewItemPayload;
import com.shutovna.topfive.entities.payload.UpdateItemPayload;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ItemService<T extends Item, N extends NewItemPayload, U extends UpdateItemPayload> {
    List<T> findAllItems();
    List<T> findAvailableForTop(Integer topId);

    Optional<T> findItem(Integer itemId);

    T createItem(N payload, Integer userId) throws IOException;

    public void updateItem(Integer itemId, U payload);

    void deleteItem(Integer itemId);

    void addToTop(Integer topId, Integer itemId);

    void removeFromTop(Integer topId, Integer itemId);
}
