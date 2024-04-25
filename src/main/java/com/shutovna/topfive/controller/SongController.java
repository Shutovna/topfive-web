package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.WebUtil;
import com.shutovna.topfive.entities.Genre;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.UpdateSongPayload;
import com.shutovna.topfive.service.DefaultSongService;
import com.shutovna.topfive.service.GenreService;
import com.shutovna.topfive.service.ItemService;
import com.shutovna.topfive.service.SongService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("songs/{songId:\\d+}")
@Slf4j
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    private final GenreService genreService;

    private final MessageSource messageSource;

    @ModelAttribute("song")
    private Song getSong(@PathVariable Integer songId) {
        return songService.findItem(songId).orElseThrow(
                () -> new NoSuchElementException("ru.shutovna.msg.song.not_found")
        );
    }

    @ModelAttribute("genres")
    public List<Genre> getGenres() {
        return genreService.findMusicGenres();
    }

    @GetMapping
    public String editSong(@ModelAttribute Song song, Model model, HttpServletRequest request) {
        log.debug("Editing {}", song);
        model.addAttribute("previousPage", WebUtil.getPreviousPageByRequest(request));
        return "songs/edit_song";
    }

    @PostMapping
    public String updateSong(@ModelAttribute(binding = false) Song song,
                             @ModelAttribute("previousPage") String previousPage,
                             @PathVariable Integer songId,
                             @Valid UpdateSongPayload songPayload,
                             BindingResult bindingResult,
                             Model model, HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("payload", songPayload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "songs/edit_song";
        }
        songService.updateItem(songId, songPayload);
        log.info("Updated song {}", songId);
        return "redirect:" + previousPage;
    }

    @PostMapping("delete")
    public String deleteSong(@ModelAttribute("previousPage") String previousPage,
                             @PathVariable Integer songId,
                             Model model) {
        songService.deleteItem(songId);
        return "redirect:" + previousPage;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response, Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
                this.messageSource.getMessage(exception.getMessage(), new Object[0],
                        exception.getMessage(), locale));
        return "errors/404";
    }

}
