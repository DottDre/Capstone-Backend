package it.epicode.erboristeria.users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO userRequestDto) {
        return ResponseEntity.ok(userService.create(userRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> modify(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDto) {
        return ResponseEntity.ok(userService.modify(id, userRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.delete(id));
    }
}
