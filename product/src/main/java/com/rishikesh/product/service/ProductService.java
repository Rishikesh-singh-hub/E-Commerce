package com.rishikesh.product.service;


import com.rishikesh.product.dto.order.OrderItemReqDto;
import com.rishikesh.product.dto.product.ProductDto;
import com.rishikesh.product.dto.product.ProductResDto;
import com.rishikesh.product.entity.ProductEntity;
import com.rishikesh.product.entity.SellerEntity;
import com.rishikesh.product.mapper.ProductMapper;
import com.rishikesh.product.repository.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepo productRepo;
    private final com.rishikesh.product.service.CloudinaryService cloudService;

    public ProductService(ProductRepo productRepo,
                          com.rishikesh.product.service.CloudinaryService cloudService) {
        this.productRepo = productRepo;
        this.cloudService = cloudService;
    }

    public ProductEntity create(ProductEntity p) {
        ProductEntity product = new ProductEntity();
        product.setId(UUID.randomUUID().toString());

        return productRepo.save(p);
    }

    public ProductEntity update(String id, ProductDto dto) {
        ProductEntity p = productRepo.findById(id).orElseThrow(() -> new RuntimeException("ProductEntity not found"));
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        return productRepo.save(p);
    }

    public ProductEntity get(String id) { return productRepo.findById(id).orElseThrow(() -> new RuntimeException("ProductEntity not found")); }

    public List<ProductResDto> list() {
        List<ProductEntity> entity =  productRepo.findAll();
         List<ProductResDto> resDtos = entity.stream().map(
                 ProductMapper::toResDto
         ).toList();
         return resDtos;
    }

    public void delete(String id) { productRepo.deleteById(id); }

    @Transactional
    public ProductResDto createProduct(ProductDto dto, MultipartFile image, String userId) {

        logger.info("got product add request...");

        Map<String, Object> imageDetail = cloudService.uploadImage(image,"Product");
        ProductEntity entity= ProductMapper.toEntity(dto);
        entity.setSellerId(userId);
        entity.setPublicId(imageDetail.get("public_id").toString());
        entity.setSecureUrl(imageDetail.get("secure_url").toString());
        productRepo.save(entity);
        return ProductMapper.toResDto(entity);

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
