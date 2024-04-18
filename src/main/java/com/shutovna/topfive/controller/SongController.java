package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.WebUtil;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import com.shutovna.topfive.service.GenreService;
import com.shutovna.topfive.service.SongService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("songs/{songId:\\d+}")
@Slf4j
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    private final GenreService genreService;

    @ModelAttribute("song")
    private Song getSong(@PathVariable Integer songId) {
        return songService.findSong(songId).orElseThrow(
                () -> new NoSuchElementException("ru.nikitos.msg.song.not_found")
        );
    }

    @GetMapping
    public String editSong(@ModelAttribute Song song, Model model, HttpServletRequest request) {
        log.debug("Editing {}", song);
        model.addAttribute("previousPage", WebUtil.getPreviousPageByRequest(request));
        model.addAttribute("genres", genreService.findGenres());
        return "songs/edit_song";
    }

    @PostMapping
    public String updateSong(@ModelAttribute(binding = false) Song song,
                             BindingResult bindingResult,
                             @ModelAttribute("previousPage") String previousPage,
                             @PathVariable Integer songId,
                             UpdateSongPayload songPayload, Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", songPayload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "songs/edit_song";
        }
        songService.updateSong(songId, songPayload);
        log.info("Updated song {}", songId);
        return "redirect:" + previousPage;
    }

    @PostMapping("delete")
    public String deleteSong(@ModelAttribute("previousPage") String previousPage,
                             @PathVariable Integer songId,
                             Model model) {
        songService.deleteSong(songId);
        return "redirect:" + previousPage;
    }
}
