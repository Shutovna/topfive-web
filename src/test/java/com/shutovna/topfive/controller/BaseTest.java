package com.shutovna.topfive.controller;

import com.shutovna.topfive.entities.User;
import com.shutovna.topfive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseTest {
    @Value("${topfive.test.username}")
    protected String testUsername;
    @Autowired
    protected UserService userService;

    protected User getTestUser() {
        return userService.loadUserByUsername(testUsername);
    }
}
