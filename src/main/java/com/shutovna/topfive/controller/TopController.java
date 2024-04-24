package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemTable;
import com.shutovna.topfive.entities.Item;
import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.payload.UpdateTopPayload;
import com.shutovna.topfive.service.ItemService;
import com.shutovna.topfive.service.TopService;
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

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("tops/{topId:\\d+}")
@Slf4j
@RequiredArgsConstructor
public class TopController {
    private final TopService topService;

    private final ItemService<Item> itemService;

    private final MessageSource messageSource;

    @ModelAttribute("top")
    private Top getTop(@PathVariable Integer topId) {
        return topService.findTop(topId).orElseThrow(
                () -> new NoSuchElementException("ru.nikitos.msg.top.not_found")
        );
    }

    @GetMapping
    public String showTop(@ModelAttribute Top top, Model model) {
        log.debug("Showing {}", top);
        ItemTable<Item> itemTable = new ItemTable<>(itemService.findAllByTop(top));
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
                            @Valid UpdateTopPayload topPayload, BindingResult bindingResult,
                            Model model, HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("top", top);
            model.addAttribute("payload", topPayload);
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

    @PostMapping("addItem")
    public String addItem(@PathVariable Integer topId,
                          @RequestParam("itemId") Integer itemId) {
        log.debug("Adding item {} to top {}", itemId, topId);
        itemService.addToTop(topId, itemId);
        return "redirect:/tops/%d".formatted(topId);
    }

    @PostMapping("removeItem")
    public String removeItem(@PathVariable("topId") Integer topId,
                                    @RequestParam("itemId") Integer itemId) {
        log.debug("Removing item {} from top {}", itemId, topId);
        itemService.removeFromTop(topId, itemId);
        return "redirect:/tops/%d".formatted(topId);
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
