package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemTable;
import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.TopType;
import com.shutovna.topfive.entities.payload.NewItemPayload;
import com.shutovna.topfive.entities.payload.UpdateItemPayload;
import com.shutovna.topfive.service.ItemService;
import com.shutovna.topfive.service.TopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/items/select")
@Slf4j
@RequiredArgsConstructor
public class ItemSelectController {
    private final ItemService<Item, NewItemPayload, UpdateItemPayload> itemService;

    private final TopService topService;

    private static final Map<TopType, String> viewsByTopType = new HashMap<>();

    static {
        viewsByTopType.put(TopType.SONG, "songs/select_song");
        viewsByTopType.put(TopType.VIDEO, "videos/select_video");
        viewsByTopType.put(TopType.PHOTO, "photos/select_photo");
    }

    @GetMapping
    public String showSongs(@RequestParam Integer topId, @RequestParam String successUrl, Model model) {
        log.debug("showSongs");
        ItemTable<Item> itemTable = new ItemTable<>(itemService.findAvailableForTop(topId));
        model.addAttribute("items", itemTable.getRows());
        model.addAttribute("topId", topId);
        model.addAttribute("successUrl", successUrl);

        Top top = topService.findTop(topId).orElseThrow();
        return viewsByTopType.get(top.getType());
    }
}
