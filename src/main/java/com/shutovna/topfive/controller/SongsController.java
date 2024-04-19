package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemTable;
import com.shutovna.topfive.controller.util.WebUtil;
import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.service.GenreService;
import com.shutovna.topfive.service.SongService;
import com.shutovna.topfive.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

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
        ItemTable<Song> itemTable = new ItemTable<>(songService.findAllSongs());
        model.addAttribute("items", itemTable.getRows());
        return "songs/song_table";
    }


    @GetMapping("create")
    public String showCreateForm(@RequestParam(required = false) Integer topId, Model model,
                                 HttpServletRequest request) {
        model.addAttribute("topId", topId);
        model.addAttribute("genres", genreService.findGenres());
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
            model.addAttribute("genres", genreService.findGenres());
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "songs/new_song";
        }

        MultipartFile file = songPayload.getFile();
        log.debug("MultipartFile: " + file);
        User user = userService.loadUserByUsername(principal.getName());
        songService.createSong(songPayload.getArtist(), songPayload.getTitle(), songPayload.getDescription(),
                songPayload.getBitRate(), songPayload.getReleasedAt(), songPayload.getGenreId(),
                songPayload.getTopId(), file.getOriginalFilename(), file.getBytes(), file.getContentType(), user.getId());
        return "redirect:" + previousPage;
    }
}
