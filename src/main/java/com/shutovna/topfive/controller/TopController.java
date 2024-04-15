package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemRow;
import com.shutovna.topfive.controller.util.ItemTable;
import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.payload.UpdateTopPayload;
import com.shutovna.topfive.service.TopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("tops/{topId:\\d+}")
@Slf4j
@RequiredArgsConstructor
public class TopController {
    private final TopService topService;

    private MessageSource messageSource;


    @ModelAttribute("top")
    private Top getTop(@PathVariable Integer topId) {
        return topService.findTop(topId).orElseThrow(
                () -> new NoSuchElementException("ru.nikitos.msg.top.not_found")
        );
    }

    @GetMapping
    public String showTop(@ModelAttribute Top top, Model model) {
        log.debug("Showing {}", top);
        ItemTable<Item> itemTable = new ItemTable<>(top.getItems());
        model.addAttribute("items", itemTable.getRows());

        switch (top.getType()) {
            case SONG -> {
                return "tops/song_top";
            }
            case VIDEO -> {
                return "tops/video_top";
            }
            case PHOTO -> {
                return "tops/photo_top";
            }
            default -> throw new IllegalStateException("Unknown " + top.getType());
        }
    }


    @GetMapping("edit")
    public String editTop(@ModelAttribute Top top) {
        log.debug("Editing {}", top);
        return "tops/edit_top";
    }

    @PostMapping("edit")
    public String updateTop(@ModelAttribute(binding = false) Top top, @PathVariable Integer topId,
                            UpdateTopPayload topPayload, BindingResult bindingResult,
                            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("top", topPayload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "tops/edit_top";
        }
        topService.updateTop(topId, topPayload.title(), topPayload.details());
        log.info("Created {}", top);
        return "redirect:/tops/%d".formatted(top.getId());
    }

    @PostMapping("delete")
    public String deleteTop(@ModelAttribute Top top, @PathVariable Integer topId, Model model) {
        topService.deleteTop(topId);
        log.info("Top {} deleted", top);
        return "redirect:/tops/table";
    }
}
