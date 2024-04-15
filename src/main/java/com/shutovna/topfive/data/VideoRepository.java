package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Integer> {
}
