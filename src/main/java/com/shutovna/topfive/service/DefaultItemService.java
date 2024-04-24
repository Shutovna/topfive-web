package com.shutovna.topfive.service;

import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.data.UserRepository;
import com.shutovna.topfive.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class DefaultItemService <T extends Item> implements ItemService<T> {
    private final static Map<TopType, Class<?>> itemClassesByTopType = new HashMap<>();

    static {
        itemClassesByTopType.put(TopType.SONG, Song.class);
        itemClassesByTopType.put(TopType.VIDEO, Video.class);
        itemClassesByTopType.put(TopType.PHOTO, Photo.class);
    }

    protected final ItemRepository<T> itemRepository;

    protected final TopRepository topRepository;

    protected final FileStorageService fileStorageService;

    protected final UserRepository userRepository;

    @Override
    public List<T> findAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<T> findAvailableForTopItems(Integer topId) {
        Top top = topRepository.findById(topId).orElseThrow(NoSuchElementException::new);
        return itemRepository.findAvailableForTop(top, itemClassesByTopType.get(top.getType()));
    }

    @Override
    public Optional<T> findItem(Integer itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public void deleteItem(Integer itemId) {
        Optional<T> song = itemRepository.findById(itemId);
        if (song.isPresent()) {
            for (Top top : new ArrayList<>(song.get().getTops())) {
                song.get().removeTop(top);
            }
            itemRepository.delete(song.get());
        }
    }

    @Override
    public void addToTop(Integer topId, Integer itemId) {
        T item = itemRepository.findById(itemId).orElseThrow(NoSuchElementException::new);
        Top top = topRepository.findById(topId).orElseThrow(NoSuchElementException::new);
        top.addItem(item);
    }

    @Override
    public void removeFromTop(Integer topId, Integer itemId) {
        T item = itemRepository.findById(itemId).orElseThrow(NoSuchElementException::new);
        Top top = topRepository.findById(topId).orElseThrow(NoSuchElementException::new);
        top.removeItem(item);
    }
}
