package com.r2s.services.user;

import com.r2s.dtos.UserDTO;
import com.r2s.dtos.UserDetail.CustomUserDetails;
import com.r2s.dtos.common.*;
import com.r2s.dtos.token.AccessTokenGenerated;
import com.r2s.entities.RoleEntity;
import com.r2s.entities.UserEntity;
import com.r2s.enums.RoleEnum;
import com.r2s.repositories.RoleRepository;
import com.r2s.repositories.UserRepository;
import com.r2s.utils.TokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, TokenUtil tokenUtil, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseDTO<LoginResponseDTO> login(LoginRequestDTO userDto) {
        try {
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassWord()));

            if (authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                if (!userDetails.isEnabled()) {
                    return ResponseDTO.<LoginResponseDTO>builder()
                            .status("NOT_FOUND_STATUS")
                            .message("USER_INACTIVE_STATUS")
                            .build();
                }

                AccessTokenGenerated accessTokenGenerated = tokenUtil.generateToken(userDetails);
                return ResponseDTO.<LoginResponseDTO>builder()
                        .status("OK")
                        .message("Login success")
                        .data(
                                LoginResponseDTO.builder()
                                        .accessToken(accessTokenGenerated.getAccessToken())
                                        .expiredIn(accessTokenGenerated.getExpiredIn())
                                        .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                                        .build()
                        ).build();
            } else {
                return ResponseDTO.<LoginResponseDTO>builder()
                        .status("NOT_FOUND_STATUS")
                        .message("USER_NOT_FOUND")
                        .build();
            }
        } catch (AuthenticationException e) {
            return ResponseDTO.<LoginResponseDTO>builder()
                    .status("NOT_FOUND_STATUS")
                    .message("INVALID_USERNAME_OR_PASSWORD")
                    .build();
        }
    }

    @Override
    public ResponseDTO<RegisterResponseDTO> register(RegisterRequestDTO userDto) {
        RoleEntity defaultRole = roleRepository.findByName(RoleEnum.USER);
        return registerForAll(userDto, defaultRole);
    }


    @Override
    public ResponseDTO<RegisterResponseDTO> adminRegister(RegisterRequestDTO userDto) {
        RoleEntity defaultRole = roleRepository.findByName(RoleEnum.ADMIN);
        return registerForAdmin(userDto, defaultRole);
    }

    private ResponseDTO<RegisterResponseDTO> registerForAll(RegisterRequestDTO userDto, RoleEntity defaultRole) {
        Optional<UserEntity> existingUser = userRepository.findByUserName(userDto.getUserName());

        if (existingUser.isPresent()) {
            return ResponseDTO.<RegisterResponseDTO>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                    .message("USERNAME_EXIST_MESSAGE")
                    .data(null)
                    .build();
        }

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setUserName(userDto.getUserName());
        newUserEntity.setPassWord(passwordEncoder.encode(userDto.getPassWord()));
        newUserEntity.setEmail(userDto.getEmail());
        newUserEntity.setStatus(true);
        newUserEntity.setFullName(userDto.getFullName());

        if (defaultRole != null) {
            Set<RoleEntity> roles = new HashSet<>();
            roles.add(defaultRole);
            newUserEntity.setRoles(roles);
        } else {
            defaultRole = new RoleEntity();
            roleRepository.save(defaultRole);

            Set<RoleEntity> roles = new HashSet<>();
            roles.add(defaultRole);
            newUserEntity.setRoles(roles);
        }

        UserEntity userEntity = userRepository.save(newUserEntity);
        try {
            return getRegisterResponseDTOResponseDTO(userDto);
        } catch (Exception e) {
            ResponseDTO<RegisterResponseDTO> responseDTO = new ResponseDTO<>();
            responseDTO.setStatus("ERROR_STATUS");
            responseDTO.setMessage("Registration failed: " + e.getMessage());

            return responseDTO;
        }
    }

    private ResponseDTO<RegisterResponseDTO> getRegisterResponseDTOResponseDTO(RegisterRequestDTO userDto) {
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();
        registerResponseDTO.setAddress(userDto.getAddress());
        registerResponseDTO.setUserName(userDto.getUserName());
        registerResponseDTO.setPassWord(passwordEncoder.encode(userDto.getPassWord()));
        registerResponseDTO.setFullName(userDto.getFullName());
        registerResponseDTO.setEmail(userDto.getEmail());

        ResponseDTO<RegisterResponseDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("SUCCESS_STATUS");
        responseDTO.setData(registerResponseDTO);
        responseDTO.setMessage("SUCCESS_MESSAGE");
        return responseDTO;
    }

    private ResponseDTO<RegisterResponseDTO> registerForAdmin(RegisterRequestDTO userDto, RoleEntity defaultRole) {
        Optional<UserEntity> existingUser = userRepository.findByUserName(userDto.getUserName());

        if (existingUser.isPresent()) {
            return ResponseDTO.<RegisterResponseDTO>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                    .message("USERNAME_EXIST_MESSAGE")
                    .data(null)
                    .build();
        }

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setUserName(userDto.getUserName());
        newUserEntity.setPassWord(passwordEncoder.encode(userDto.getPassWord()));
        newUserEntity.setStatus(true);

        if (defaultRole != null) {
            Set<RoleEntity> roles = new HashSet<>();
            roles.add(defaultRole);
            newUserEntity.setRoles(roles);
        } else {
            defaultRole = new RoleEntity();
            roleRepository.save(defaultRole);

            Set<RoleEntity> roles = new HashSet<>();
            roles.add(defaultRole);
            newUserEntity.setRoles(roles);
        }

        UserEntity userEntity = userRepository.save(newUserEntity);
        try {
            return getRegisterResponseDTOAdmin(userDto);
        } catch (Exception e) {
            ResponseDTO<RegisterResponseDTO> responseDTO = new ResponseDTO<>();
            responseDTO.setStatus("ERROR_STATUS");
            responseDTO.setMessage("Registration failed: " + e.getMessage());

            return responseDTO;
        }
    }

    private ResponseDTO<RegisterResponseDTO> getRegisterResponseDTOAdmin(RegisterRequestDTO userDto) {
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();
        registerResponseDTO.setUserName(userDto.getUserName());
        registerResponseDTO.setPassWord(passwordEncoder.encode(userDto.getPassWord()));

        ResponseDTO<RegisterResponseDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("SUCCESS_STATUS");
        responseDTO.setData(registerResponseDTO);
        responseDTO.setMessage("SUCCESS_MESSAGE");
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<UserDTO>> findAll() {
        return null;
    }

    @Override
    public ResponseDTO<UserDTO> findById(Long id) {
        return null;
    }

    @Override
    public ResponseDTO<UserDTO> create(UserDTO dto) {
        return null;
    }

    @Override
    public ResponseDTO<UserDTO> update(Long id, UserDTO dto) {
        return null;
    }

    @Override
    public ResponseDTO<Void> delete(Long id) {
        return null;
    }

}


