package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemTable;
import com.shutovna.topfive.controller.util.WebUtil;
import com.shutovna.topfive.entities.Genre;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.service.GenreService;
import com.shutovna.topfive.service.SongService;
import com.shutovna.topfive.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/songs")
@Slf4j
@RequiredArgsConstructor
public class SongsController {
    private final SongService songService;
    private final GenreService genreService;
    private final UserService userService;

    @GetMapping("table")
    public String showSongs(Model model) {
        log.debug("showSongs");
        ItemTable<Song> itemTable = new ItemTable<>(songService.findAllItems());
        model.addAttribute("items", itemTable.getRows());
        return "songs/song_table";
    }

    @ModelAttribute("genres")
    public List<Genre> getGenres() {
        return genreService.findMusicGenres();
    }


    @GetMapping("create")
    public String showCreateForm(@RequestParam(required = false) Integer topId, Model model,
                                 HttpServletRequest request) {
        model.addAttribute("topId", topId);
        model.addAttribute("previousPage", WebUtil.getPreviousPageByRequest(request));
        return "songs/new_song";
    }

    @PostMapping("create")
    public String createSong(@Valid NewSongPayload songPayload,
                             BindingResult bindingResult,
                             @ModelAttribute("previousPage") String previousPage,
                             Model model, Principal principal, HttpServletResponse response) throws IOException {
        log.debug("Creating " + songPayload);

        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("payload", songPayload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "songs/new_song";
        }

        MultipartFile file = songPayload.getFile();
        log.debug("MultipartFile: " + file);
        User user = userService.loadUserByUsername(principal.getName());
        songService.createItem(songPayload, user.getId());
        return "redirect:" + previousPage;
    }
}
