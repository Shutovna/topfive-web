package com.shutovna.topfive.service;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.data.UserRepository;
import com.shutovna.topfive.entities.*;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@Transactional
public class DefaultSongService extends DefaultItemService<Song> implements SongService {
    private final GenreRepository genreRepository;

    public DefaultSongService(ItemRepository<Song> itemRepository, TopRepository topRepository,
                              FileStorageService fileStorageService,
                              UserRepository userRepository, GenreRepository genreRepository) {
        super(itemRepository, topRepository, fileStorageService, userRepository);
        this.genreRepository = genreRepository;
    }

    @Override
    public Song createItem(NewSongPayload payload, Integer userId) throws IOException {
        Song song = new Song();
        song.setTitle(payload.getTitle());
        song.setDescription(payload.getDescription());
        song.setUser(userRepository.getReferenceById(userId));
        song.setArtist(payload.getArtist());
        song.setBitRate(payload.getBitRate());
        song.setReleasedAt(payload.getReleasedAt());
        song.setGenre(genreRepository.getReferenceById(payload.getGenreId()));
        if (payload.getTopId() != null) {
            song.addTop(topRepository.findById(payload.getTopId()).orElseThrow());
        }

        MultipartFile file = payload.getFile();
        ItemData itemData = new ItemData();
        itemData.setFilename(file.getOriginalFilename());
        itemData.setContentType(file.getContentType());
        song.setData(itemData);

        fileStorageService.createItemDataFile(song.getData().getFilename(), file.getBytes());

        return itemRepository.save(song);
    }

    @Override
    public void updateItem(Integer itemId, UpdateSongPayload payload) {
        Song song = itemRepository.findById(itemId).orElseThrow(NoSuchElementException::new);
        song.setTitle(payload.getTitle());
        song.setDescription(payload.getDescription());
        song.setArtist(payload.getArtist());
        song.setBitRate(payload.getBitRate());
        song.setReleasedAt(payload.getReleasedAt());
        song.setGenre(genreRepository.getReferenceById(payload.getGenreId()));
    }
}
