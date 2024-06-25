package it.epicode.erboristeria.users;


import it.epicode.erboristeria.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserResponseDTO responseDto = new UserResponseDTO();
                    BeanUtils.copyProperties(user, responseDto);
                    return responseDto;
                }).collect(Collectors.toList());
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
        UserResponseDTO responseDto = new UserResponseDTO();
        BeanUtils.copyProperties(user, responseDto);
        return responseDto;
    }

    @Transactional
    public UserResponseDTO create(@Valid UserRequestDTO UserRequestDTO) {
        User user = new User();
        BeanUtils.copyProperties(UserRequestDTO, user);
        userRepository.save(user);
        UserResponseDTO responseDto = new UserResponseDTO();
        BeanUtils.copyProperties(user, responseDto);
        return responseDto;
    }

    public UserResponseDTO modify(Long id, @Valid UserRequestDTO UserRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
        BeanUtils.copyProperties(UserRequestDTO, user);
        userRepository.save(user);
        UserResponseDTO responseDto = new UserResponseDTO();
        BeanUtils.copyProperties(user, responseDto);
        return responseDto;
    }

    public String delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found for this id :: " + id);
        }
        userRepository.deleteById(id);
        return "User deleted";
    }
}

