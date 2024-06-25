package com.r2s.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponseDTO{
    private String name;
    private String image;
    private String description;
    private Double unitPrice;
    private Integer unitsInStock;
}
