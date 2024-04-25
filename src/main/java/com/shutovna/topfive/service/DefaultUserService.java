package com.shutovna.topfive.service;

import com.shutovna.topfive.data.RoleRepository;
import com.shutovna.topfive.data.UserRepository;
import com.shutovna.topfive.entities.Role;
import com.shutovna.topfive.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User '" + username + "' not found");
    }

    @Override
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) {
            throw new UserAlreadyExistsException("ru.shutovna.msg.user.error.already_exists");
        }
        User u = (User) user;
        Role roleUser = roleRepository.findByName(RoleRepository.ROLE_USER);
        u.getRoles().add(roleUser);
        userRepository.save(u);
    }

    @Override
    public void updateUser(UserDetails user) {
        userRepository.save((User) user);
    }

    @Override
    public void deleteUser(String username) {
        User user = loadUserByUsername(username);
        userRepository.delete(user);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean userExists(String username) {
        try {
            loadUserByUsername(username);
            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }
}
