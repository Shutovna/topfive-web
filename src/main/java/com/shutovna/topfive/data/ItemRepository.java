package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.entities.Top;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository<T extends Item> extends JpaRepository<T, Integer> {
    @Query("select i from Item i where TYPE(i) = :cls and :top not member of i.tops order by i.id desc")
    List<T> findAvailableForTop(@Param("top") Top top, @Param("cls") Class<?> cls);

    @Query("select i from Item i where TYPE(i) = :cls order by i.id desc")
    List<T> findAllByClass(@Param("cls") Class<?> cls);

    @Query("select i from Item i where :top member of i.tops order by i.id desc")
    List<T> findAllByTop(Top top);

}
