package com.shutovna.topfive.data;

import com.shutovna.topfive.entities.Role;
import com.shutovna.topfive.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    private User user;
    private Role role_user;
    private Role role_admin;

    @BeforeEach
    public void createUserAndRoles() {
        role_user = new Role("ROLE_USER2");
        entityManager.persist(role_user);
        role_admin = new Role("ROLE_ADMIN2");
        entityManager.persist(role_admin);

        user = new User(null, "testUser", "testPassword");
        user.getRoles().add(role_user);
        user.getRoles().add(role_admin);
        user = userRepository.saveAndFlush(user);
    }

    @Test
    public void testCreate() {
        assertNotNull(user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertEquals(List.of(role_user, role_admin), user.getRoles());
    }

    @Test
    public void testUpdate() {
        user.setUsername("anotherName");
        user.setPassword("anotherPassword");
        user.getRoles().remove(role_admin);

        userRepository.saveAndFlush(user);
        User userDB = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("anotherName", userDB.getUsername());
        assertEquals("anotherPassword", userDB.getPassword());
        assertEquals(List.of(role_user), userDB.getRoles());
    }
}
