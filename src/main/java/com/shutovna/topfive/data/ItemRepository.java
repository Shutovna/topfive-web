package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository<T extends Item> extends JpaRepository<T, Integer> {
    public List<Song> findAllByArtistLikeIgnoreCase(String artist);
}
