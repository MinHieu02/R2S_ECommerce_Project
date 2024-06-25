package com.r2s.services;

import com.r2s.dtos.common.ResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IService<D> {

    ResponseDTO<List<D>> findAll();

    ResponseDTO<D> findById(Long id);

    ResponseDTO<D> create(D dto);

    ResponseDTO<D> update(Long id, D dto);

    ResponseDTO<Void> delete(Long id);
}
