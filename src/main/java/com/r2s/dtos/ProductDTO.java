package com.r2s.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String itemCode;
    private String name;
    private Double unitPrice;
    private Integer unitsInStock;
    private String description;
    private String manufacturer;
    private String categoryName;
    private String productCondition;
    private String image;
}
