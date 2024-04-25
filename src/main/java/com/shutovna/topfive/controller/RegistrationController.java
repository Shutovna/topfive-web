package com.shutovna.topfive.controller;

import com.shutovna.topfive.entities.payload.RegistrationPayload;
import com.shutovna.topfive.service.DefaultUserService;
import com.shutovna.topfive.service.UserAlreadyExistsException;
import com.shutovna.topfive.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final MessageSource messageSource;

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(@Valid RegistrationPayload payload, BindingResult bindingResult,
                                      HttpServletResponse response, Model model, Locale locale) {
        log.debug("Registering " + payload);

        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "registration";
        }

        try {
            userService.createUser(payload.toUser(passwordEncoder));
        } catch (UserAlreadyExistsException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("payload", payload);
            model.addAttribute("errors", List.of(Objects.requireNonNull(
                    this.messageSource.getMessage(e.getMessage(),
                            new Object[]{payload.getUsername()},
                            e.getMessage(), locale))));
            return "registration";
        }
        return "redirect:/login";
    }
}
