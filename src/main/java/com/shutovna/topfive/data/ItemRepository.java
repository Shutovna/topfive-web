package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Integer> {
}
