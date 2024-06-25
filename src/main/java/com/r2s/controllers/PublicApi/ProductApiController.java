package com.r2s.controllers.PublicApi;

import com.r2s.dtos.ProductDetailResponseDTO;
import com.r2s.dtos.ProductListResponseDTO;
import com.r2s.dtos.common.ResponseDTO;
import com.r2s.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductApiController {

    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<ProductListResponseDTO>>> findListProduct() {
        ResponseDTO<List<ProductListResponseDTO>> response = productService.findListProduct();
        HttpStatus httpStatus = response.getStatus().equals(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())) ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProductDetailResponseDTO>> findDetailProduct(@PathVariable Long id) {
        ResponseDTO<ProductDetailResponseDTO> response = productService.findDetailProduct(id);
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(response.getStatus()));
        return ResponseEntity.status(httpStatus).body(response);
    }
}

