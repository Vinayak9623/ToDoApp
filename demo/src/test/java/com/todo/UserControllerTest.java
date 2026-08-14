package com.todo;

import com.todo.controller.UserController;
import com.todo.model.User;
import com.todo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        userController = new UserController(userService);
    }

    @Test
    void testGetAllUsers() {
        ResponseEntity<List<User>> response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 3);
    }

    @Test
    void testGetUserById_Found() {
        ResponseEntity<?> response = userController.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof User);
        User user = (User) response.getBody();
        assertEquals("Alice Johnson", user.getName());
    }

    @Test
    void testGetUserById_NotFound() {
        ResponseEntity<?> response = userController.getUserById(999L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateUser() {
        User newUser = new User("David Miller", "david@example.com", "QA Engineer");
        ResponseEntity<User> response = userController.createUser(newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("David Miller", response.getBody().getName());
        assertEquals("david@example.com", response.getBody().getEmail());
        assertEquals("QA Engineer", response.getBody().getRole());
    }

    @Test
    void testUpdateUser_Found() {
        User updateData = new User("Alice Updated", "alice.updated@example.com", "Engineering Lead");
        ResponseEntity<?> response = userController.updateUser(1L, updateData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof User);
        User updated = (User) response.getBody();
        assertEquals("Alice Updated", updated.getName());
        assertEquals("alice.updated@example.com", updated.getEmail());
        assertEquals("Engineering Lead", updated.getRole());
    }

    @Test
    void testUpdateUser_NotFound() {
        User updateData = new User("Ghost", "ghost@example.com", "Unknown");
        ResponseEntity<?> response = userController.updateUser(999L, updateData);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteUser_Found() {
        // Create a user first
        User user = userService.createUser(new User("Temp User", "temp@example.com", "Intern"));
        Long tempId = user.getId();

        ResponseEntity<Map<String, Object>> response = userController.deleteUser(tempId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));

        // Verify it was removed
        Optional<User> found = userService.getUserById(tempId);
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteUser_NotFound() {
        ResponseEntity<Map<String, Object>> response = userController.deleteUser(999L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
    }
}
