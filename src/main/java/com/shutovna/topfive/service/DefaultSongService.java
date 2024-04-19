package com.shutovna.topfive.service;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.data.UserRepository;
import com.shutovna.topfive.entities.*;
import com.shutovna.topfive.entities.payload.ItemPayload;
import com.shutovna.topfive.entities.payload.NewItemPayload;
import com.shutovna.topfive.entities.payload.NewSongPayload;
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
public class DefaultSongService extends DefaultItemService<Song, NewSongPayload, UpdateSongPayload> {
    private final GenreRepository genreRepository;

    public DefaultSongService(ItemRepository<Song> itemRepository, TopRepository topRepository,
                              FileStorageService fileStorageService,
                              UserRepository userRepository, GenreRepository genreRepository) {
        super(itemRepository, topRepository, fileStorageService, userRepository);
        this.genreRepository = genreRepository;
    }

    @Override
    Song createItemObject() {
        return new Song();
    }

    @Override
    void fillItemForCreate(Song item, NewSongPayload payload) {
        item.setArtist(payload.getArtist());
        item.setBitRate(payload.getBitRate());
        item.setReleasedAt(payload.getReleasedAt());
        item.setGenre(genreRepository.getReferenceById(payload.getGenreId()));

    }

    @Override
    void fillItemForUpdate(Song item, UpdateSongPayload payload) {
        item.setArtist(payload.getArtist());
        item.setBitRate(payload.getBitRate());
        item.setReleasedAt(payload.getReleasedAt());
        item.setGenre(genreRepository.getReferenceById(payload.getGenreId()));
    }
}
