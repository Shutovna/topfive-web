package com.shutovna.topfive.service;

import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.data.UserRepository;
import com.shutovna.topfive.entities.*;
import com.shutovna.topfive.entities.payload.NewItemPayload;
import com.shutovna.topfive.entities.payload.UpdateItemPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class DefaultItemService
        <T extends Item, N extends NewItemPayload, U extends UpdateItemPayload> implements ItemService<T, N, U> {
    private final ItemRepository<T> itemRepository;

    private final TopRepository topRepository;

    private final FileStorageService fileStorageService;

    private final UserRepository userRepository;

    T createItemObject() {
        throw new UnsupportedOperationException();
    }

    void fillItemForCreate(T item, N payload) {
        throw new UnsupportedOperationException();
    }

    void fillItemForUpdate(T item, U payload) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> findAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<T> findAvailableForTopItems(Integer topId) {
        Top top = topRepository.findById(topId).orElseThrow(NoSuchElementException::new);
        return itemRepository.findAvailableForTop(top, itemClassesByTopType.get(top.getType()));
    }

    static Map<TopType, Class<?>> itemClassesByTopType = new HashMap<>();

    static {
        itemClassesByTopType.put(TopType.SONG, Song.class);
        itemClassesByTopType.put(TopType.VIDEO, Video.class);
        itemClassesByTopType.put(TopType.PHOTO, Photo.class);
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
    public T createItem(N payload, Integer userId) throws IOException {
        T item = createItemObject();
        item.setTitle(payload.getTitle());
        item.setDescription(payload.getDescription());
        item.setUser(userRepository.getReferenceById(userId));
        if (payload.getTopId() != null) {
            item.addTop(topRepository.findById(payload.getTopId()).orElseThrow());
        }

        MultipartFile file = payload.getFile();
        ItemData itemData = new ItemData();
        itemData.setFilename(file.getOriginalFilename());
        itemData.setContentType(file.getContentType());
        item.setData(itemData);

        fillItemForCreate(item, payload);

        fileStorageService.createItemDataFile(item.getData().getFilename(), file.getBytes());

        return itemRepository.save(item);
    }

    @Override
    public void updateItem(Integer itemId, U payload) {
        T item = itemRepository.findById(itemId).orElseThrow(NoSuchElementException::new);
        item.setTitle(payload.getTitle());
        item.setDescription(payload.getDescription());

        fillItemForUpdate(item, payload);
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
