package com.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String role;

    // Explicit constructor without ID (useful when creating new users)
    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
