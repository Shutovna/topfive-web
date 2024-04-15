package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemRow;
import com.shutovna.topfive.controller.util.ItemTable;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.service.GenreService;
import com.shutovna.topfive.service.SongService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/songs")
@Slf4j
@RequiredArgsConstructor
public class SongsController {
    private final SongService songService;
    private final GenreService genreService;

    @GetMapping
    public String showSongs(Model model) {
        ItemTable<Song> itemTable = new ItemTable<>(songService.findAllSongs());
        model.addAttribute("items", itemTable.getRows());
        return "songs/songs_table";
    }


    @GetMapping("create")
    public String showCreateForm(@RequestParam Integer topId, Model model) {
        model.addAttribute("topId", topId);
        model.addAttribute("song", new Song());
        model.addAttribute("genres", genreService.findGenres());
        return "songs/new_song";
    }

    @PostMapping("create")
    public String createSong(@RequestParam("file") MultipartFile file,
                             NewSongPayload songPayload,
                             BindingResult bindingResult,
                             @ModelAttribute("topId") Integer topId, Model model,
                             Principal principal) {
        log.debug("Creating " + songPayload);
        log.debug("MultipartFile: " + file);

        try {
            String originalFilename = file.getOriginalFilename();
            log.debug("contentType = " + file.getContentType());
            songPayload.setType(file.getContentType());
            byte[] data = file.getBytes();
            if (!StringUtils.isEmpty(originalFilename) && !ArrayUtils.isEmpty(data)) {
                songPayload.setFileName(originalFilename);
                songPayload.setData(data);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", songPayload);
            model.addAttribute("genres", genreService.findGenres());
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "tops/new_top";
        }
        songService.createSong(songPayload, principal.getName());
        return "redirect:/tops/%d".formatted(topId);
    }
}
