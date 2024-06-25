package it.epicode.erboristeria.security;


import lombok.Data;

@Data
public class LoginResponseDTO {
    String user;
    String token;

    public LoginResponseDTO(String username, String jwt) {
        this.user = username;
        this.token = jwt;
    }


}
