package com.shutovna.topfive.controller.util;

import com.shutovna.topfive.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ItemRow<T extends Item> {
    private T item;
    private String url;
}
