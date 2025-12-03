package com.rishikesh.app.controller;

import com.rishikesh.app.dto.product.ProductDto;
import com.rishikesh.app.dto.product.ProductResDto;
import com.rishikesh.app.entity.ProductEntity;
import com.rishikesh.app.jwt.JwtUtils;
import com.rishikesh.app.service.ProductService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;
    private final JwtUtils jwtUtils;
    Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService svc, JwtUtils jwtUtils) { this.svc = svc;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/check")
    public String  check(@RequestPart("image") MultipartFile file,@RequestPart ("data") ProductDto dto){
        logger.info(dto.getName());
        logger.info(file.getContentType());
        return "Ok";
    }

    @PreAuthorize("hasAnyRole('Seller')")
    @PostMapping("/auth")
    public ResponseEntity<ProductResDto> create(@RequestPart("data") ProductDto dto,
                                                @RequestPart("image") MultipartFile image,
                                                HttpServletRequest req) {

        String userId = getUserIdFromRequest(req);
       ProductResDto resDto=  svc.createProduct(dto,image,userId);
        return ResponseEntity.ok(resDto);
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

    private String getUserIdFromRequest(HttpServletRequest request){

        String jwt = request.getHeader("Authorization").substring(7);
        Claims claims = jwtUtils.parseClaims(jwt);

        String userId = claims.getSubject();
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("missing subject");
        }
        logger.info("return user id from jwt : {}",userId);
        return userId;
    }
}
