package com.r2s.services.role;

import com.r2s.dtos.RoleDTO;
import com.r2s.dtos.common.ResponseDTO;
import com.r2s.entities.RoleEntity;
import com.r2s.enums.RoleEnum;
import com.r2s.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseDTO<List<RoleDTO>> findAll() {
        List<RoleEntity> list = roleRepository.findAll();

        if (list.isEmpty()) {
            return ResponseDTO.<List<RoleDTO>>builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND))
                    .message("NOT_FOUND_LIST_MESSAGE")
                    .build();
        }

        List<RoleDTO> listRes = list.stream()
                .map(roleEntity -> modelMapper.map(roleEntity, RoleDTO.class))
                .collect(Collectors.toList());

        return ResponseDTO.<List<RoleDTO>>builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("FOUND_LIST_MESSAGE")
                .data(listRes)
                .build();
    }

    @Override
    public ResponseDTO<RoleDTO> findById(Long id) {
        Optional<RoleEntity> optionalRole = roleRepository.findById(id);

        if (optionalRole.isPresent()) {
            RoleDTO roleDTO = modelMapper.map(optionalRole.get(), RoleDTO.class);
            return ResponseDTO.<RoleDTO>builder()
                    .status(String.valueOf(HttpStatus.OK.value()))
                    .message("FOUND_MESSAGE")
                    .data(roleDTO)
                    .build();
        } else {
            return ResponseDTO.<RoleDTO>builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .message("NOT_FOUND_MESSAGE")
                    .build();
        }
    }

    @Override
    public ResponseDTO<RoleDTO> create(RoleDTO roleDTO) {
        try {
            RoleEntity roleEntity = modelMapper.map(roleDTO, RoleEntity.class);
            RoleEntity savedRole = roleRepository.save(roleEntity);
            RoleDTO savedRoleDTO = modelMapper.map(savedRole, RoleDTO.class);

            return ResponseDTO.<RoleDTO>builder()
                    .status(String.valueOf(HttpStatus.CREATED.value()))
                    .message("CREATE_SUCCESS_MESSAGE")
                    .data(savedRoleDTO)
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<RoleDTO>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("CREATE_FAILED_MESSAGE")
                    .build();
        }
    }

    @Override
    public ResponseDTO<RoleDTO> update(Long id, RoleDTO roleDTO) {
        try {
            Optional<RoleEntity> optionalRole = roleRepository.findById(id);

            if (optionalRole.isPresent()) {
                RoleEntity existingRole = optionalRole.get();
                existingRole.setName(RoleEnum.valueOf(roleDTO.getName()));
                existingRole.setDescription(roleDTO.getDescription());

                RoleEntity updatedRole = roleRepository.save(existingRole);
                RoleDTO updatedRoleDTO = modelMapper.map(updatedRole, RoleDTO.class);

                return ResponseDTO.<RoleDTO>builder()
                        .status(String.valueOf(HttpStatus.OK.value()))
                        .message("UPDATE_SUCCESS_MESSAGE")
                        .data(updatedRoleDTO)
                        .build();
            } else {
                return ResponseDTO.<RoleDTO>builder()
                        .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .message("UPDATE_NOT_FOUND_MESSAGE")
                        .build();
            }
        } catch (Exception e) {
            return ResponseDTO.<RoleDTO>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("UPDATE_FAILED_MESSAGE")
                    .build();
        }
    }

    @Override
    public ResponseDTO<Void> delete(Long id) {
        try {
            Optional<RoleEntity> optionalRole = roleRepository.findById(id);

            if (optionalRole.isPresent()) {
                roleRepository.deleteById(id);

                return ResponseDTO.<Void>builder()
                        .status(String.valueOf(HttpStatus.OK.value()))
                        .message("DELETE_SUCCESS_MESSAGE")
                        .build();
            } else {
                return ResponseDTO.<Void>builder()
                        .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .message("DELETE_NOT_FOUND_MESSAGE")
                        .build();
            }
        } catch (Exception e) {
            return ResponseDTO.<Void>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("DELETE_FAILED_MESSAGE")
                    .build();
        }
    }
}
