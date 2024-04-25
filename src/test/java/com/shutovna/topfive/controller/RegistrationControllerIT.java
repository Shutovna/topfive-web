package com.shutovna.topfive.controller;

import com.shutovna.topfive.controller.util.ItemRow;
import com.shutovna.topfive.data.GenreRepository;
import com.shutovna.topfive.data.RoleRepository;
import com.shutovna.topfive.entities.Role;
import com.shutovna.topfive.entities.Song;
import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.entities.payload.NewSongPayload;
import com.shutovna.topfive.entities.payload.RegistrationPayload;
import com.shutovna.topfive.service.SongService;
import com.shutovna.topfive.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegistrationControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Test
    void getRegisterPage_ReturnsRegisterPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/register");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("registration")
                );
    }

    @Test
    void registerUser_RequestIsValid_RedirectToLogin() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/register")
                .param("username", "NewUser")
                .param("password", "password123")
                .param("confirmPassword", "password123")
                .with(csrf());


        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string(HttpHeaders.LOCATION, "/login")
                );

        User user = userService.loadUserByUsername("NewUser");
        assertEquals("NewUser", user.getUsername());
        assertTrue(user.getPassword().startsWith("$2a$"));
        assertEquals(1, user.getRoles().size());
        Role roleUser = roleRepository.findByName(RoleRepository.ROLE_USER);
        assertEquals(roleUser, user.getRoles().get(0));
    }

    @Test
    void registerUser_RequestIsInValid_ReturnsRegistrationPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/register")
                .param("username", "")
                .param("password", "pass")
                .param("confirmPassword", "pass123")
                .with(csrf());


        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        view().name("registration"),
                        model().attribute("payload", new RegistrationPayload(
                                "", "pass", "pass123")),
                        model().attribute("errors",
                                Matchers.containsInAnyOrder(
                                        "Имя пользователя должно быть указано",
                                        "Имя пользователя должно быть от 3 до 20 символов",
                                        "Пароль должен быть от 5 до 20 символов",
                                        "Пароли не совпадают")
                        )
                );

    }

    @Test
    void registerUser_UserExists_ReturnsRegistrationPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/register")
                .param("username", "shutovna")
                .param("password", "password123")
                .param("confirmPassword", "password123")
                .with(csrf());


        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        view().name("registration"),
                        model().attribute("payload", new RegistrationPayload(
                                "shutovna", "password123", "password123")),
                        model().attribute("errors",
                                Matchers.contains(
                                        "Пользователь shutovna уже существует"
                        )
                ));
    }
}
