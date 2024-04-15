package com.shutovna.topfive.controller;

import com.shutovna.topfive.entities.payload.NewTopPayload;
import com.shutovna.topfive.entities.Top;
import com.shutovna.topfive.entities.TopType;
import com.shutovna.topfive.service.TopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/tops")
@Slf4j
@RequiredArgsConstructor
public class TopsController {
    private final TopService topService;


    @ModelAttribute(name = "topTypes")
    public Enum<TopType>[] topTypes() {
        return TopType.values();
    }

    @GetMapping("table")
    public String showTops(Model model, @RequestParam(value = "filter", required = false) String filter) {
        List<Top> tops = topService.findAllTops(filter);
        log.debug("Showing {} tops", tops.size());
        model.addAttribute("tops", tops);
        model.addAttribute("filter", filter);
        return "tops/top_table";
    }

    @GetMapping("create")
    public String showCreateTop(Model model) {
        return "tops/new_top";
    }

    @PostMapping("create")
    public String createTop(@Valid NewTopPayload topPayload, BindingResult bindingResult, Model model,
                            Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("top", topPayload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "tops/new_top";
        }
        Top top = topService.createTop(topPayload.topType(), topPayload.title(),
                topPayload.details(), principal.getName());
        log.info("Created {}", top);
        return "redirect:/tops/%d".formatted(top.getId());
    }
}
