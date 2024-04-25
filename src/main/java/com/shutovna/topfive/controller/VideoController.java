package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.WebUtil;
import com.shutovna.topfive.entities.Genre;
import com.shutovna.topfive.entities.Video;
import com.shutovna.topfive.entities.payload.UpdateVideoPayload;
import com.shutovna.topfive.service.GenreService;
import com.shutovna.topfive.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("videos/{videoId:\\d+}")
@Slf4j
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    private final GenreService genreService;

    private final MessageSource messageSource;

    @ModelAttribute("video")
    private Video getVideo(@PathVariable Integer videoId) {
        return videoService.findItem(videoId).orElseThrow(
                () -> new NoSuchElementException("ru.shutovna.msg.video.not_found")
        );
    }

    @ModelAttribute("genres")
    public List<Genre> getGenres() {
        return genreService.findVideoGenres();
    }

    @GetMapping
    public String editVideo(@ModelAttribute Video video, Model model, HttpServletRequest request) {
        log.debug("Editing {}", video);
        model.addAttribute("previousPage", WebUtil.getPreviousPageByRequest(request));
        return "videos/edit_video";
    }

    @PostMapping
    public String updateVideo(@ModelAttribute(binding = false) Video video,
                              @ModelAttribute("previousPage") String previousPage,
                              @PathVariable Integer videoId,
                              @Valid UpdateVideoPayload videoPayload,
                              BindingResult bindingResult,
                              Model model, HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("payload", videoPayload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "videos/edit_video";
        }
        videoService.updateItem(videoId, videoPayload);
        log.info("Updated video {}", videoId);
        return "redirect:" + previousPage;
    }

    @PostMapping("delete")
    public String deleteVideo(@ModelAttribute("previousPage") String previousPage,
                              @PathVariable Integer videoId) {
        videoService.deleteItem(videoId);
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
