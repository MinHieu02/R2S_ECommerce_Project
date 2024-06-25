package com.r2s.controllers.AdminApi;

import com.r2s.dtos.CategoryDTO;
import com.r2s.dtos.common.ResponseDTO;
import com.r2s.services.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryAdminApiController {

    private final CategoryService categoryService;

    public CategoryAdminApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<CategoryDTO>> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        ResponseDTO<CategoryDTO> response = categoryService.create(categoryDTO);
        HttpStatus httpStatus = response.getStatus().equals("404 NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO<CategoryDTO>> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO updatedCategoryDTO) {
        ResponseDTO<CategoryDTO> response = categoryService.update(id, updatedCategoryDTO);
        HttpStatus httpStatus = response.getStatus().equals("404 NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable Long id) {
        ResponseDTO<Void> response = categoryService.delete(id);
        HttpStatus httpStatus = response.getStatus().equals("404 NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus).body(response);
    }
}
