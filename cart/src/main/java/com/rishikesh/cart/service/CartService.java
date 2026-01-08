package com.rishikesh.cart.service;


import com.rishikesh.cart.dto.AddToCartReqDto;
import com.rishikesh.cart.dto.CartResDto;
import com.rishikesh.cart.entity.CartEntity;
import com.rishikesh.cart.entity.CartItemEntity;
import com.rishikesh.cart.entity.Status;
import com.rishikesh.cart.mapper.CartMapper;
import com.rishikesh.cart.repository.CartRepo;
import com.rishikesh.contracts.ProductEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {
    Logger logger = LoggerFactory.getLogger(CartService.class);


    private final WebClient userClient;
    private final CartRepo cartRepo;
    private final WebClient productClient;

    public CartService(@Qualifier("userClient") WebClient webClientConfig,
                       CartRepo cartRepo,
                       @Qualifier("productClient") WebClient productRepo) {
        this.userClient = webClientConfig;
        this.cartRepo = cartRepo;
        this.productClient = productRepo;
    }

    public CartResDto getOrCreateCart(String userId) {
        CartEntity cart= cartRepo.findByUserId(userId).orElse(null);
        if(cart == null){
            cart = newCart(userId);
        }
        return CartMapper.toResponse(cart);

    }

    private CartEntity newCart(String userId){

        UserEntity entity = userClient.get()
                .uri("/{userId}",userId)
                .retrieve()
                .bodyToMono(UserEntity.class)
                .block();

            return CartEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(entity.getId())
                    .status(Status.DRAFT)
                    .updatedAt(Instant.now())
                    .build();

    }

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

    public CartResDto clearCart(String userId) {
        CartEntity cart = CartMapper.toEntity(getOrCreateCart(userId),userId);
        cart.getItems().clear();
        return CartMapper.toResponse(cartRepo.save(cart));
    }

    private ProductEntity getProductById(String id, String authToken){

        return productClient.get()
                .uri("/auth/{id}",id)
                .headers(header->{
                    header.setBearerAuth(authToken);
                })
                .retrieve()
                .bodyToMono(ProductEntity.class)
                .timeout(Duration.ofSeconds(3))
                .block();
    }

    public CartResDto addItem(String userId, String productId, int qty, String authToken) {

        CartEntity cart = CartMapper.toEntity(getOrCreateCart(userId),userId);
        ProductEntity newProductEntity = getProductById(productId,authToken);
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
