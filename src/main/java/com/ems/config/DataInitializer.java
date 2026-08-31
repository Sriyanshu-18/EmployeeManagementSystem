package com.ems.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ems.entity.Role;
import com.ems.entity.User;
import com.ems.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            User admin = userRepository
                    .findByUsername("admin")
                    .orElse(null);

            if (admin == null) {

                admin = new User(
                        "admin",
                        "admin@example.com",
                        passwordEncoder.encode("admin123"),
                        Role.ADMIN
                );

                userRepository.save(admin);

                System.out.println("=================================");
                System.out.println("DEFAULT ADMIN USER CREATED");
                System.out.println("Username: admin");
                System.out.println("Password: admin123");
                System.out.println("=================================");

            } else {

                // Force reset password temporarily
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );

                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("=================================");
                System.out.println("ADMIN PASSWORD RESET");
                System.out.println("Username: admin");
                System.out.println("Password: admin123");
                System.out.println("=================================");
            }
        };
    }
}