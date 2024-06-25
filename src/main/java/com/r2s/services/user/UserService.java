package com.r2s.services.user;

import com.r2s.dtos.UserDTO;
import com.r2s.dtos.common.*;
import com.r2s.services.IService;
import org.springframework.stereotype.Service;
@Service
public interface UserService extends IService<UserDTO> {
    ResponseDTO<LoginResponseDTO> login(LoginRequestDTO userDTO);
    ResponseDTO<RegisterResponseDTO> register(RegisterRequestDTO userDTO);
    ResponseDTO<RegisterResponseDTO> adminRegister(RegisterRequestDTO userDto);
}
