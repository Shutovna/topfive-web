package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Top;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TopRepository extends JpaRepository<Top, Integer> {
    List<Top> findAllByTitleLikeIgnoreCase(String filter);
}
