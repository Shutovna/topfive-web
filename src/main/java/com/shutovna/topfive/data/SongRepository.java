package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {
    public List<Song> findAllByArtistLikeIgnoreCase(String artist);
}
