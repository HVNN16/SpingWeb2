package donga.edu.demo.controllers.api.dto;

import donga.edu.demo.models.User;

public record UserDto(Long id, String name, String email, String role) {
    public static UserDto from(User u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail(), u.getRole());
    }
}
