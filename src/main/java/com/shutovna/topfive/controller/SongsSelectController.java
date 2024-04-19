package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemTable;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/songs/select")
@Slf4j
@RequiredArgsConstructor
public class SongsSelectController {
    private final SongService songService;

    @GetMapping
    public String showSongs(@RequestParam Integer topId, Model model) {
        log.debug("showSongs");
        ItemTable<Song> itemTable = new ItemTable<>(songService.findAllSongs());
        model.addAttribute("items", itemTable.getRows());
        model.addAttribute("topId", topId);
        return "songs/select_song";
    }

    @PostMapping
    public String selectSong(@ModelAttribute("topId") Integer topId,
                           @ModelAttribute("songId") Integer songId) {
        log.debug("Adding song {} to top {}", songId, topId);
        songService.addToTop(topId, songId);
        return "redirect:/tops/%d".formatted(topId);
    }
}
