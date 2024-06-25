package com.r2s.config;

import com.r2s.entities.RoleEntity;
import com.r2s.entities.UserEntity;
import com.r2s.enums.RoleEnum;
import com.r2s.repositories.RoleRepository;
import com.r2s.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Load initial data into the database
        RoleEnum ROLE_ADMIN = RoleEnum.ADMIN;
        String USERNAME = "admin";
        String PASSWORD = "Abc@12345";

        RoleEntity roleAdmin = roleRepository.findByName(ROLE_ADMIN);
        if (roleAdmin == null) {
            roleAdmin = new RoleEntity(ROLE_ADMIN);
            roleRepository.save(roleAdmin);
        }

        Optional<UserEntity> adminUserOptional = userRepository.findByUserName(USERNAME);
        if (adminUserOptional.isEmpty()) {
            UserEntity adminUser = new UserEntity();
            adminUser.setUserName(USERNAME);
            adminUser.setPassWord(passwordEncoder.encode(PASSWORD)); //use spring-boot-starter-security
            adminUser.setStatus(true);
            adminUser.getRoles().add(roleAdmin);
            userRepository.save(adminUser);
        }
    }
}
