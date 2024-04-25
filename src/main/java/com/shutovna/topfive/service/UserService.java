package com.shutovna.topfive.service;

import com.shutovna.topfive.entities.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserService extends UserDetailsManager {
    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
