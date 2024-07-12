package it.epicode.erboristeria.security;


import it.epicode.erboristeria.users.Role;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String user;
    private String token;
    private Role role;

    public LoginResponseDTO(String username, String jwt, Role role) {
        this.user = username;
        this.token = jwt;
        this.role = role;
    }


}
