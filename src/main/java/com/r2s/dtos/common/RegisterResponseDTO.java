package com.r2s.dtos.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDTO {
    private String userName;
    private String passWord;
    private String address;
    private String email;
    private String fullName;
}
