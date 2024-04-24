package com.shutovna.topfive.service;

import com.shutovna.topfive.data.TopRepository;
import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.TopType;
import com.shutovna.topfive.entities.User;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultTopService implements TopService {
    private final TopRepository topRepository;

    @Override
    public List<Top> findAllTops(String filter) {
        if (StringUtils.isEmpty(filter)) {
            return topRepository.findAll();
        } else {
            return topRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        }
        
    }

    @Override
    public Top createTop(TopType topType, String title, String details, User user) {
        Top top = new Top();
        top.setType(topType);
        top.setTitle(title);
        top.setDetails(details);
        top.setUser(user);
        return topRepository.save(top);
    }

    @Override
    public Optional<Top> findTop(Integer topId) {
        return topRepository.findById(topId);
    }

    @Override
    public void updateTop(Integer topId, String title, String details) {
        this.topRepository.findById(topId)
                .ifPresentOrElse(top -> {
                            top.setTitle(title);
                            top.setDetails(details);
                        }, () -> {
                            throw new NoSuchElementException("ru.nikitos.msg.top.not_found");
                        }
                );
    }

    @Override
    public void deleteTop(Integer topId) {
        topRepository.deleteById(topId);
    }
}
