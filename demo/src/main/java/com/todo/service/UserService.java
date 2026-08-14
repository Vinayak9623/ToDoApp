package com.todo.service;

import com.todo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    // Thread-safe in-memory list to store users without a database
    private final List<User> userList = new CopyOnWriteArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserService() {
        // Pre-populate with initial demo data
        userList.add(new User(idGenerator.getAndIncrement(), "Alice Johnson", "alice@example.com", "Developer"));
        userList.add(new User(idGenerator.getAndIncrement(), "Bob Smith", "bob@example.com", "DevOps Engineer"));
        userList.add(new User(idGenerator.getAndIncrement(), "Charlie Brown", "charlie@example.com", "Product Manager"));
    }

    // READ: Get all users
    public List<User> getAllUsers() {
        return userList;
    }

    // READ: Get user by ID
    public Optional<User> getUserById(Long id) {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    // CREATE: Add new user
    public User createUser(User user) {
        user.setId(idGenerator.getAndIncrement());
        userList.add(user);
        return user;
    }

    // UPDATE: Update an existing user by ID
    public Optional<User> updateUser(Long id, User updatedUser) {
        return getUserById(id).map(existingUser -> {
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }
            return existingUser;
        });
    }

    // DELETE: Delete user by ID
    public boolean deleteUser(Long id) {
        return userList.removeIf(user -> user.getId().equals(id));
    }
}
