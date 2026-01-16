package com.rishikesh.product.controller;

import com.rishikesh.product.dto.product.ProductDto;
import com.rishikesh.product.dto.product.ProductResDto;
import com.rishikesh.product.entity.ProductEntity;
import com.rishikesh.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;
    Logger logger = LoggerFactory.getLogger(ProductController.class);


    public ProductController(ProductService svc) {
        this.svc = svc;
    }
    

    @PreAuthorize("hasAnyRole('Seller')")
    @PostMapping("/auth/add-product")
    public ResponseEntity<ProductResDto> create(@RequestPart("data") ProductDto dto,
                                                @RequestPart("image") MultipartFile image,
                                                @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
       ProductResDto resDto=  svc.createProduct(dto,image,userId);
        return ResponseEntity.ok(resDto);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ProductEntity> get(@PathVariable String id) {

        return ResponseEntity.ok(svc.get(id));
    }

    @GetMapping("/public/{productName}")
    public ResponseEntity<?> getByName(@PathVariable String productName) {
        List<ProductDto>  dtos = svc.getByName(productName);
        if(dtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/public/all")
    public ResponseEntity<List<ProductResDto>> list() { return ResponseEntity.ok(svc.list()); }

    @PutMapping("/auth/{id}")
    public ResponseEntity<ProductEntity> update(@PathVariable String id, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(svc.update(id, dto));
    }

    @DeleteMapping("/auth/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

}
