package com.r2s.dtos.token;

import com.r2s.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenGenerated {

    private String refreshToken;
    private Date expiredIn;
    private UserEntity user;
}
