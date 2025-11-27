package com.rishikesh.app.controller;


import com.rishikesh.app.dto.cart.AddToCartReqDto;
import com.rishikesh.app.dto.cart.CartResDto;
import com.rishikesh.app.entity.CartEntity;
import com.rishikesh.app.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService svc;

    public CartController(CartService svc) { this.svc = svc; }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResDto> getCart(@PathVariable String userId,
                                              HttpServletRequest request) {

        CartResDto resDto = svc.getOrCreateCart(userId);
        return ResponseEntity.ok(resDto);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResDto> addItem(
            @PathVariable String userId,
            @Valid @RequestBody AddToCartReqDto req) {

        CartResDto resDto = svc.addItem(userId, req.getProductId(), req.getQuantity());
        return ResponseEntity.ok(resDto);
    }

    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<?> remove(@PathVariable String userId,
                                    @Valid @RequestBody AddToCartReqDto req)
                                            throws ChangeSetPersister.NotFoundException {
        CartResDto resDto = svc.removeItem(userId,req);
        if (resDto != null){
            return ResponseEntity.ok(resDto);
        }
        return ResponseEntity.badRequest().body("Cart Empty");
    }

    @PostMapping("/{userId}/clear")
    public ResponseEntity<CartEntity> clear(@PathVariable String userId) {
        return ResponseEntity.ok(svc.clearCart(userId));
    }
}
