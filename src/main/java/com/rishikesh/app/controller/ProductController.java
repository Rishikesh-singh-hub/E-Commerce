package com.rishikesh.app.controller;

import com.rishikesh.app.dto.product.ProductDto;
import com.rishikesh.app.entity.ProductEntity;
import com.rishikesh.app.jwt.JwtUtils;
import com.rishikesh.app.service.ProductService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;
    private final JwtUtils jwtUtils;

    public ProductController(ProductService svc, JwtUtils jwtUtils) { this.svc = svc;
        this.jwtUtils = jwtUtils;
    }

    @PreAuthorize("hasAnyRole('Seller')")
    @PostMapping("/auth")
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto, HttpServletRequest req) {

        String userId = getUserIdFromRequest(req);
       String id =  svc.createProduct(dto,userId);
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

    private String getUserIdFromRequest(HttpServletRequest request){

        String jwt = request.getHeader("Authorization").substring(7);
        Claims claims = jwtUtils.parseClaims(jwt);

        String userId = claims.getSubject();
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("missing subject");
        }
        return userId;
    }
}
