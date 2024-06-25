package it.epicode.erboristeria.security;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisteredUserDTO {
    Long id;
    String firstName;
    String lastName;
    String username;
    String email;


    @Builder(setterPrefix = "with")
    public RegisteredUserDTO(Long id, String firstName, String lastName, String username, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;

    }
}
