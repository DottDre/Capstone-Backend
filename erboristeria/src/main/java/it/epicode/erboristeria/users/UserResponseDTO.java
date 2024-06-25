package it.epicode.erboristeria.users;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
}
