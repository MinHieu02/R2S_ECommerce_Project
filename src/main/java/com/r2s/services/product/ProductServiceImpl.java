package com.r2s.services.product;

import com.r2s.dtos.ProductDTO;
import com.r2s.dtos.ProductDetailResponseDTO;
import com.r2s.dtos.ProductListResponseDTO;
import com.r2s.dtos.common.ResponseDTO;
import com.r2s.entities.CategoryEntity;
import com.r2s.entities.ProductEntity;
import com.r2s.enums.ConditionEnum;
import com.r2s.repositories.CategoryRepository;
import com.r2s.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseDTO<List<ProductListResponseDTO>> findListProduct() {
        try {
            List<ProductEntity> products = productRepository.findAll();
            List<ProductListResponseDTO> productDTOs = products.stream()
                    .map(product -> {
                        ProductListResponseDTO dto = new ProductListResponseDTO();
                        dto.setName(product.getName());
                        dto.setImage(product.getImage());
                        dto.setDescription(product.getDescription());
                        dto.setUnitPrice(product.getUnitPrice());
                        dto.setUnitsInStock(product.getUnitsInStock());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseDTO.<List<ProductListResponseDTO>>builder()
                    .status(String.valueOf(HttpStatus.OK.value()))
                    .message("Products retrieved successfully")
                    .data(productDTOs)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.<List<ProductListResponseDTO>>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("Error retrieving products")
                    .build();
        }
    }


    @Override
    public ResponseDTO<ProductDetailResponseDTO> findDetailProduct(Long id) {
        try {
            Optional<ProductEntity> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) {
                return ResponseDTO.<ProductDetailResponseDTO>builder()
                        .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .message("Product not found")
                        .build();
            }

            ProductEntity product = productOptional.get();
            ProductDetailResponseDTO detailDTO = new ProductDetailResponseDTO();
            detailDTO.setName(product.getName());
            detailDTO.setImage(product.getImage());
            detailDTO.setDescription(product.getDescription());
            detailDTO.setItemCode(product.getItemCode());
            detailDTO.setManufacturer(product.getManufacturer());
            detailDTO.setUnitsInStock(product.getUnitsInStock());
            detailDTO.setUnitPrice(product.getUnitPrice());

            if (product.getCategory() != null) {
                detailDTO.setCategoryName(product.getCategory().getName());
            }

            return ResponseDTO.<ProductDetailResponseDTO>builder()
                    .status(String.valueOf(HttpStatus.OK.value()))
                    .message("Product details retrieved successfully")
                    .data(detailDTO)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.<ProductDetailResponseDTO>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("Error retrieving product details")
                    .build();
        }
    }

    @Override
    public ResponseDTO<List<ProductDTO>> findAll() {
        return null;
    }

    @Override
    public ResponseDTO<ProductDTO> findById(Long id) {
        return null;
    }

    @Override
    public ResponseDTO<ProductDTO> create(ProductDTO productDTO) {
        ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);

        Optional<CategoryEntity> categoryOptional = categoryRepository.findByName(productDTO.getCategoryName());
        if (categoryOptional.isEmpty()) {
            return ResponseDTO.<ProductDTO>builder()
                    .status("404 NOT_FOUND")
                    .message("Category not found")
                    .build();
        }
        productEntity.setCategory(categoryOptional.get());

        if (!ConditionEnum.contains(productDTO.getProductCondition())) {
            return ResponseDTO.<ProductDTO>builder()
                    .status("400 BAD_REQUEST")
                    .message("Invalid condition value")
                    .build();
        }

        // Đặt đường dẫn ảnh từ productDTO
        productEntity.setImage(productDTO.getImage());

        if (productDTO.getItemCode() == null || productDTO.getItemCode().isEmpty()) {
            productEntity.setItemCode(productEntity.generateRandomItemCode());
        }

        productRepository.save(productEntity);

        ProductDTO savedProductDTO = modelMapper.map(productEntity, ProductDTO.class);

        return ResponseDTO.<ProductDTO>builder()
                .status("201 CREATED")
                .message("Product created successfully")
                .data(savedProductDTO)
                .build();
    }

    @Override
    public ResponseDTO<ProductDTO> update(Long id, ProductDTO productDTO) {
        Optional<ProductEntity> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseDTO.<ProductDTO>builder()
                    .status("404 NOT_FOUND")
                    .message("Product not found")
                    .build();
        }

        ProductEntity productEntity = productOptional.get();
        modelMapper.map(productDTO, productEntity);

        // Thêm dòng log để kiểm tra thông tin của ProductEntity trước khi lưu
        System.out.println("ProductEntity before save: " + productEntity);

        productRepository.save(productEntity);

        // Thêm dòng log để kiểm tra thông tin của ProductEntity sau khi lưu
        System.out.println("ProductEntity after save: " + productEntity);

        ProductDTO updatedProductDTO = modelMapper.map(productEntity, ProductDTO.class);

        return ResponseDTO.<ProductDTO>builder()
                .status("200 OK")
                .message("Product updated successfully")
                .data(updatedProductDTO)
                .build();
    }


    @Override
    public ResponseDTO<Void> delete(Long id) {
        return null;
    }
}
