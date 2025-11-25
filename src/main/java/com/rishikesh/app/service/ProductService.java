package com.rishikesh.app.service;


import com.rishikesh.app.dto.product.ProductDto;
import com.rishikesh.app.entity.ProductEntity;
import com.rishikesh.app.mapper.ProductMapper;
import com.rishikesh.app.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public ProductEntity create(ProductEntity p) { return productRepo.save(p); }

    public ProductEntity update(String id, ProductDto dto) {
        ProductEntity p = productRepo.findById(id).orElseThrow(() -> new RuntimeException("ProductEntity not found"));
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        return productRepo.save(p);
    }

    public ProductEntity get(String id) { return productRepo.findById(id).orElseThrow(() -> new RuntimeException("ProductEntity not found")); }

    public List<ProductEntity> list() { return productRepo.findAll(); }

    public void delete(String id) { productRepo.deleteById(id); }

    public String createProduct(ProductDto dto) {

        ProductEntity entity= ProductMapper.toEntity(dto);
        productRepo.save(entity);
        return entity.getId();

    }
}