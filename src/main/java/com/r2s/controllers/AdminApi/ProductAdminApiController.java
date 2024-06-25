package com.r2s.controllers.AdminApi;

import com.r2s.dtos.ProductDTO;
import com.r2s.dtos.common.ResponseDTO;
import com.r2s.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductAdminApiController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<ProductDTO>> create(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart("image") MultipartFile imageFile) {

        // Gọi service để lưu trữ product và nhận lại product đã được lưu với ID
        ResponseDTO<ProductDTO> responseDTO = productService.create(productDTO);
        ProductDTO savedProductDTO = responseDTO.getData();

        // Xử lý lưu trữ file image và lấy đường dẫn
        if (!imageFile.isEmpty() && savedProductDTO != null) {
            try {
                // Đường dẫn lưu trữ file với product ID
                String uploadDir = "src/main/resources/static/images/" + savedProductDTO.getId() + "/";
                String fileName = imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);

                // Tạo thư mục nếu chưa tồn tại
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Lưu file vào thư mục
                Files.write(filePath, imageFile.getBytes());

                // Đặt đường dẫn vào productDTO
                savedProductDTO.setImage("/images/" + savedProductDTO.getId() + "/" + fileName);

                // Cập nhật lại product với đường dẫn ảnh mới
                productService.create(savedProductDTO);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(
                        ResponseDTO.<ProductDTO>builder()
                                .status("400 BAD_REQUEST")
                                .message("Error saving image file")
                                .build()
                );
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

}
