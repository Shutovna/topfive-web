package com.shutovna.topfive.controller.util;

import com.shutovna.topfive.controller.ItemDownloadController;
import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.entities.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ItemTable<T extends Item> {
    private final Collection<T> rows;

    public List<ItemRow<T>> getRows() {
        return rows.stream().map(
                item -> new ItemRow<>(item, "/files/" + item.getData().getFilename())
        ).toList();
    }
}
