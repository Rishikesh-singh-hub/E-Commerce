package com.rishikesh.app.service;


import com.rishikesh.app.dto.order.OrderItemReqDto;
import com.rishikesh.app.dto.product.ProductDto;
import com.rishikesh.app.entity.ProductEntity;
import com.rishikesh.app.mapper.ProductMapper;
import com.rishikesh.app.repository.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);
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

    public ProductEntity validateProduct(OrderItemReqDto productReq) {

        ProductEntity product =productRepo.findByIdAndStockGreaterThanEqual(productReq.getProductId(),productReq.getQuantity()).orElse(null);
        if (product != null) {

            ProductEntity userProduct = ProductEntity.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(productReq.getQuantity())
                    .build();

            return userProduct;
        }
        return null;
    }

    public  void  updateProduct(boolean add, int qty, ProductEntity product){

        ProductEntity stockProduct = productRepo.findById(product.getId()).orElse(null);


        if (add) {

            ProductEntity updatedProduct = ProductEntity.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(stockProduct.getStock() + product.getStock())
                    .build();
            productRepo.save(updatedProduct);
        }else{
            ProductEntity updatedProduct = ProductEntity.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(stockProduct.getStock() - product.getStock())
                    .build();
            productRepo.save(updatedProduct);
        }


    }

    public List<ProductDto> getByName(String productName) {

        List<ProductEntity> productEntities = productRepo.findAllByNameContainingIgnoreCase(productName);
        List<ProductDto> dtos = productEntities.stream().map(ProductMapper::toDto).toList();
        return dtos;

    }
}