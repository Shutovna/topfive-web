package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.entities.Top;

import java.util.List;
import java.util.Optional;

public interface ItemService<T extends Item> {
    List<T> findAllItems();

    List<T> findAllItemsByClass(Class<?> cls);

    List<T> findAvailableForTopItems(Integer topId);

    List<T> findAllByTop(Top top);

    Optional<T> findItem(Integer itemId);

    void deleteItem(Integer itemId);

    void addToTop(Integer topId, Integer itemId);

    void removeFromTop(Integer topId, Integer itemId);

}
