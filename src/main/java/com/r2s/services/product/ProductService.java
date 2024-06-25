package com.r2s.services.product;


import com.r2s.dtos.ProductDTO;
import com.r2s.dtos.ProductDetailResponseDTO;
import com.r2s.dtos.ProductListResponseDTO;
import com.r2s.dtos.common.ResponseDTO;
import com.r2s.services.IService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ProductService extends IService<ProductDTO> {

    ResponseDTO<List<ProductListResponseDTO>> findListProduct();

    ResponseDTO<ProductDetailResponseDTO> findDetailProduct(Long id);
}
