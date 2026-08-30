package com.ems.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ems.dto.UserDTO;
import com.ems.entity.Role;
import com.ems.entity.User;
import com.ems.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserDTO userDTO) {

        if (userRepository.existsByUsername(
                userDTO.getUsername())) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(
                userDTO.getEmail())) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        User user = new User();

        user.setUsername(userDTO.getUsername());

        user.setEmail(userDTO.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        userDTO.getPassword()
                )
        );

        // Default role for public registration
        user.setRole(Role.USER);

        return userRepository.save(user);
    }
}