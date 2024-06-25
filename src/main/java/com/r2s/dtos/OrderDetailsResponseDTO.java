package com.r2s.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponseDTO {

    private Long id;
    private ProductInOrderDTO product;
    private int quantity;
    private Double unitPrice;
}
