package com.r2s.config;

import com.r2s.dtos.ProductDTO;
import com.r2s.entities.ProductEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<ProductDTO, ProductEntity> typeMap = modelMapper.createTypeMap(ProductDTO.class, ProductEntity.class);
        typeMap.addMappings(mapper -> mapper.skip(ProductEntity::setImage));

        // Add a reverse type map for ProductEntity to ProductDTO
        modelMapper.createTypeMap(ProductEntity.class, ProductDTO.class)
                .addMappings(mapper -> mapper.map(ProductEntity::getImage, ProductDTO::setImage));

        return modelMapper;
    }
}
