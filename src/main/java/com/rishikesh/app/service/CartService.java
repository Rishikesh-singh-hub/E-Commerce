package com.rishikesh.app.service;


import com.rishikesh.app.dto.cart.AddToCartReqDto;
import com.rishikesh.app.dto.cart.CartItemDto;
import com.rishikesh.app.dto.cart.CartResDto;
import com.rishikesh.app.entity.CartEntity;
import com.rishikesh.app.entity.CartItemEntity;
import com.rishikesh.app.entity.ProductEntity;
import com.rishikesh.app.mapper.CartMapper;
import com.rishikesh.app.repository.CartItemRepo;
import com.rishikesh.app.repository.CartRepo;
import com.rishikesh.app.repository.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartService {
    Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;

    public CartService(CartRepo cartRepo, CartItemRepo cartItemRepo, ProductRepo productRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
    }

    public CartResDto getOrCreateCart(String userId) {
        CartEntity cartEntity= cartRepo.findByUserId(userId)
                .orElseGet(() -> cartRepo.save(CartEntity.builder().userId(userId).build()));
        return CartMapper.toResponse(cartEntity);

    }

//    public CartResDto addItem(String userId, String productId, int qty) {
//
//        ProductEntity productEntity = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
//
//        CartEntity cart = CartMapper.toEntity(getOrCreateCart(userId),userId);
//        logger.info("items in cart {}",cart.getItems().size());
//        List<CartItemEntity> existing = cart.getItems();
//        CartItemEntity itemEntity =existing.stream()
//                .filter(i -> i.getProductId().equals(productId))
//                .findFirst().orElse(null);
//        if (itemEntity == null) {
//            CartItemEntity cartItemEntity = CartItemEntity.builder()
//                    .id(UUID.randomUUID().toString())
//                    .productId(productEntity.getId())
//                    .productName(productEntity.getName())
//                    .price(productEntity.getPrice())
//                    .quantity(qty)
//                    .build();
//            existing.add(cartItemEntity);
//            logger.info("product added to cart with id : {}",cartItemEntity.getProductId());
//        } else {
//            itemEntity.setQuantity(itemEntity.getQuantity() + qty);
//        }
//        cart.setItems(existing);
//
//        logger.info("total after adding items in cart {}",cart.getItems().size());
//         cartRepo.save(cart);
//        return CartMapper.toResponse(cart);
//    }

    public CartResDto removeItem(String userId, AddToCartReqDto reqDto) throws ChangeSetPersister.NotFoundException {
        CartEntity cart = CartMapper.toEntity(getOrCreateCart(userId),userId);

       List<CartItemEntity> cartItemEntityList= cart.getItems();
       logger.info("cart items found {}",cartItemEntityList.size());
        if(!cartItemEntityList.isEmpty()){
            logger.info("cart not empty");
            CartItemEntity itemEntity = cartItemEntityList.stream()
                    .filter(item -> reqDto.getProductId().equals(item.getProductId()))
                    .findFirst()
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            int current = itemEntity.getQuantity();
            logger.info("got {} items in cart",current);
            if (reqDto.getQuantity() >= current) {
                // remove the CartItem object from the list
                boolean removed = cart.getItems().remove(itemEntity);
                if (!removed) {
                    throw new IllegalStateException("Failed to remove item");
                }
            } else {
                itemEntity.setQuantity(current - reqDto.getQuantity());
            }
            return CartMapper.toResponse(cartRepo.save(cart));


        }
        return null;
    }

    public CartEntity clearCart(String userId) {
        CartEntity cart = CartMapper.toEntity(getOrCreateCart(userId),userId);
        cart.getItems().clear();
        return cartRepo.save(cart);
    }

    public CartResDto addItem(String userId, String productId, int qty) {

        CartEntity cart = CartMapper.toEntity(getOrCreateCart(userId),userId);
        ProductEntity newProductEntity = productRepo.findById(productId).orElseThrow();
        logger.info("checking cart...");
        if (!cart.getItems().isEmpty()){
            List<CartItemEntity> cartItemList = cart.getItems();
            logger.info("{} products found in cart",cartItemList.size());
            CartItemEntity cartItem = cart.getItems()
                    .stream()
                    .filter(item ->
                            item.getProductId()
                                    .equals(productId))
                    .findFirst()
                    .orElse(null);
            if (cartItem == null){

                cartItemList.add(
                        CartItemEntity.builder()
                                .id(UUID.randomUUID().toString())
                                .productId(newProductEntity.getId())
                                .price(newProductEntity.getPrice())
                                .productName(newProductEntity.getName())
                                .quantity(qty)
                                .build()
                );

            }else {
                // if not null
                int productInCart = cartItem.getQuantity();
                cartItem.setQuantity(productInCart + qty);
            }

            cart.setItems(cartItemList);

        }else{
            logger.info("no products found in cart...");
            cart.getItems().add(
                    CartItemEntity.builder()
                            .id(UUID.randomUUID().toString())
                            .productId(newProductEntity.getId())
                            .productName(newProductEntity.getName())
                            .price(newProductEntity.getPrice())
                            .quantity(qty)
                            .build()
            );
        }
        cartRepo.save(cart);
        return CartMapper.toResponse(cart);
    }
}
