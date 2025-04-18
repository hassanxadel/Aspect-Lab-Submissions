package com.example.lab5.config;

import com.example.lab5.user.Role;
import com.example.lab5.user.User;
import com.example.lab5.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if not exists
            if (userRepository.findByUsername("hassan").isEmpty()) {
                User admin = new User();
                admin.setUsername("hassan");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Created ADMIN user 'hassan'");
            }
            
            // Create regular user if not exists
            if (userRepository.findByUsername("normal").isEmpty()) {
                User user = new User();
                user.setUsername("normal");
                user.setPassword(passwordEncoder.encode("user"));
                user.setRole(Role.USER);
                userRepository.save(user);
                System.out.println("Created USER user 'normal'");
            }
        };
    }
} 