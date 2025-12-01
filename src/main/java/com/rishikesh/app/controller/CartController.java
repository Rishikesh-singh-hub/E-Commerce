package com.rishikesh.app.controller;


import com.rishikesh.app.dto.cart.AddToCartReqDto;
import com.rishikesh.app.dto.cart.CartResDto;
import com.rishikesh.app.jwt.JwtUtils;
import com.rishikesh.app.service.CartService;
import io.jsonwebtoken.Claims;
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
    private final JwtUtils jwtUtils;

    public CartController(CartService svc, JwtUtils jwtUtils) { this.svc = svc;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public ResponseEntity<CartResDto> getCart(HttpServletRequest request) {

        String userId = getUserIdFromRequest(request);
        CartResDto resDto = svc.getOrCreateCart(userId);
        return ResponseEntity.ok(resDto);
    }

    @PostMapping("/add")
    public ResponseEntity<CartResDto> addItem(
            HttpServletRequest servletRequest,
            @Valid @RequestBody AddToCartReqDto req) {

        String userId = getUserIdFromRequest(servletRequest);
        CartResDto resDto = svc.addItem(userId, req.getProductId(), req.getQuantity());
        return ResponseEntity.ok(resDto);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(HttpServletRequest request,
                                    @Valid @RequestBody AddToCartReqDto req)
                                            throws ChangeSetPersister.NotFoundException {
        String userId = getUserIdFromRequest(request);
        CartResDto resDto = svc.removeItem(userId,req);
        if (resDto != null){
            return ResponseEntity.ok(resDto);
        }
        return ResponseEntity.badRequest().body("Cart Empty");
    }

    @GetMapping("/clear")
    public ResponseEntity<CartResDto> clear(HttpServletRequest servletRequest) {
        String userId = getUserIdFromRequest(servletRequest);
        return ResponseEntity.ok(svc.clearCart(userId));
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
