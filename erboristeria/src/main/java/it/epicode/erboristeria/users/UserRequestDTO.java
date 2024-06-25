package it.epicode.erboristeria.users;

import lombok.Data;

@Data
public class UserRequestDTO {
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private Role role;
    }

