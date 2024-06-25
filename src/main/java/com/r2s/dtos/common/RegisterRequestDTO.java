package com.r2s.dtos.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    private String userName;
    private String passWord;
    private String email;
    private String fullName;
    private String address;
}
