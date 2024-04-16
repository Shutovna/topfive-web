package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.TopType;
import com.shutovna.topfive.entities.User;

import java.util.List;
import java.util.Optional;

public interface TopService {
    List<Top> findAllTops(String filter);

    Top createTop(TopType topType, String title, String details, User user);

    Optional<Top> findTop(Integer topId);

    void updateTop(Integer topId, String title, String details);

    void deleteTop(Integer topId);
}
