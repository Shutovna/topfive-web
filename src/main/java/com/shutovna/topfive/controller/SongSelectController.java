package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemTable;
import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/songs/select")
@Slf4j
@RequiredArgsConstructor
public class SongSelectController {
    private final ItemService<Item> itemService;

    @GetMapping
    public String showSongs(@RequestParam Integer topId, @RequestParam String successUrl, Model model) {
        log.debug("showSongs");
        ItemTable<Item> itemTable = new ItemTable<>(itemService.findAllItems());
        model.addAttribute("items", itemTable.getRows());
        model.addAttribute("topId", topId);
        model.addAttribute("successUrl", successUrl);
        return "songs/select_song";
    }
}
