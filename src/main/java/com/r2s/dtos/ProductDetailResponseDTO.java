package com.r2s.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponseDTO {
    private String name;
    private String image;
    private String description;
    private String itemCode;
    private String manufacturer;
    private String categoryName;
    private Integer unitsInStock;
    private Double unitPrice;
}
