package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.entities.Top;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository<T extends Item> extends JpaRepository<T, Integer> {
    @Query("select i from Item i where TYPE(i) = :type and :top not member of i.tops")
    List<T> findAvailableForTop(@Param("top") Top top, @Param("type") Class<?> type);

    @Query("select i from Item i where TYPE(i) = :type")
    List<T> findAllByClass(@Param("type") Class<?> type);
}
