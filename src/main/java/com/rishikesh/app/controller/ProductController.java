package com.rishikesh.app.controller;

import com.rishikesh.app.dto.product.ProductDto;
import com.rishikesh.app.entity.ProductEntity;
import com.rishikesh.app.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;

    public ProductController(ProductService svc) { this.svc = svc; }

    @PostMapping("/auth")
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {

       String id =  svc.createProduct(dto);
        return ResponseEntity.created(URI.create("/api/products/" + id)).body(dto);
    }

    @GetMapping("/auth/{id}")
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
    public ResponseEntity<List<ProductEntity>> list() { return ResponseEntity.ok(svc.list()); }

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
