package com.r2s.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String userName;
    private String passWord;
    private String email;
    private String fullName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<RoleDTO> roleDTOSet;
}

