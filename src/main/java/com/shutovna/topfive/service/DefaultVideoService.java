package com.shutovna.topfive.service;

import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.data.ItemRepository;
import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.data.UserRepository;
import com.shutovna.topfive.entities.ItemData;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.Video;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.NewVideoPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import com.shutovna.topfive.entities.payload.UpdateVideoPayload;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@Transactional
public class DefaultVideoService extends DefaultItemService<Video> implements VideoService {
    private final GenreRepository genreRepository;

    public DefaultVideoService(ItemRepository<Video> itemRepository, TopRepository topRepository,
                               FileStorageService fileStorageService,
                               UserRepository userRepository, GenreRepository genreRepository) {
        super(itemRepository, topRepository, fileStorageService, userRepository);
        this.genreRepository = genreRepository;
    }

    @Override
    public Video createItem(NewVideoPayload payload, Integer userId) throws IOException {
        Video video = new Video();
        video.setTitle(payload.getTitle());
        video.setDescription(payload.getDescription());
        video.setUser(userRepository.getReferenceById(userId));
        video.setPlace(payload.getPlace());
        video.setActors(payload.getActors());
        video.setDirector(payload.getDirector());
        video.setReleasedYear(payload.getReleasedYear());
        video.setGenre(genreRepository.getReferenceById(payload.getGenreId()));
        if (payload.getTopId() != null) {
            video.addTop(topRepository.findById(payload.getTopId()).orElseThrow());
        }

        MultipartFile file = payload.getFile();
        ItemData itemData = new ItemData();
        itemData.setFilename(file.getOriginalFilename());
        itemData.setContentType(file.getContentType());
        video.setData(itemData);

        fileStorageService.createItemDataFile(video.getData().getFilename(), file.getBytes());

        return itemRepository.save(video);
    }

    @Override
    public void updateItem(Integer itemId, UpdateVideoPayload payload) {
        Video video = itemRepository.findById(itemId).orElseThrow(NoSuchElementException::new);
        video.setTitle(payload.getTitle());
        video.setDescription(payload.getDescription());
        video.setPlace(payload.getPlace());
        video.setActors(payload.getActors());
        video.setDirector(payload.getDirector());
        video.setReleasedYear(payload.getReleasedYear());
        video.setGenre(genreRepository.getReferenceById(payload.getGenreId()));
    }
}
