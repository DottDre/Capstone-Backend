package it.epicode.erboristeria.security;

import it.epicode.erboristeria.users.Role;
import it.epicode.erboristeria.users.User;
import it.epicode.erboristeria.users.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@Valid @RequestBody LoginModel loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication);

        // Ottieni le informazioni sull'utente autenticato dall'oggetto Authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new LoginResponseDTO(userDetails.getUsername(), jwt));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserModel signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        log.info(String.valueOf(signUpRequest));
        User user = new User(signUpRequest.username(),
                encoder.encode(signUpRequest.password()),
                signUpRequest.firstName(),
                signUpRequest.lastName(),
                signUpRequest.email(),
                Role.USER);
        log.info(user.toString());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
