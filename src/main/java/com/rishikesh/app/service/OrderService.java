package com.rishikesh.app.service;

import com.rishikesh.app.dto.bill.BillResDto;
import com.rishikesh.app.dto.order.OrderItemReqDto;
import com.rishikesh.app.dto.order.OrderReqDto;
import com.rishikesh.app.entity.*;
import com.rishikesh.app.mapper.BillMapper;
import com.rishikesh.app.mapper.OrderMapper;
import com.rishikesh.app.repository.BillRepo;
import com.rishikesh.app.repository.OrderRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final BillRepo billRepo;
    private final IdempotencyService idempotencyService;
    private final OrderRepo orderRepo;
    private final ProductService productService;

    public OrderService(BillRepo billRepo, IdempotencyService idempotencyService, OrderRepo orderRepo, ProductService productService){
        this.billRepo = billRepo;

        this.idempotencyService = idempotencyService;
        this.orderRepo = orderRepo;
        this.productService = productService;
    }


    private BigDecimal total(List<ProductEntity> productEntities,
                                   List<OrderItemReqDto> orderItemsDtoList) {

        Map<String, ProductEntity> entityMap = productEntities.stream()
                .collect(Collectors.toMap(ProductEntity::getId, p -> p));

        BigDecimal total = BigDecimal.ZERO;
        List<ProductEntity> updatedProducts = new ArrayList<>();

        for (OrderItemReqDto item : orderItemsDtoList) {

            ProductEntity orig = entityMap.get(item.getProductId());
            if (orig == null) {
                throw new RuntimeException("Product not found: " + item.getProductId());
            }

            int qty = item.getQuantity();

            BigDecimal line = orig.getPrice().multiply(BigDecimal.valueOf(qty));
            total = total.add(line);

        }
        return total;
    }

    public BillResDto generateBill(String userId,OrderReqDto orderRequest){

        logger.info("Checking and removing if any previous draft with user is found....");

        long removed = billRepo.deleteByUserIdAndStatus(userId,Status.DRAFT);
        logger.info("removed draft bill: {}",removed);
        orderRepo.deleteByUserIdAndStatus(userId,Status.DRAFT);
        logger.info("generating Bill request received...");
        List<OrderItemReqDto> orderItemsDtoList = orderRequest.getItems();

        List<ProductEntity> productEntities =orderRequest.getItems().stream().map(
                productService::validateProduct
        ).toList();

        logger.info("Validated Products from stocks ...");


        List<OrderItem> orderItems = productEntities.stream()
                .map(OrderMapper::toOrderItem)
                .toList();

        OrderEntity newOrder = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .idempotencyKey(null)
                .items(orderItems)
                .status(Status.DRAFT)
                .total(total(productEntities,orderItemsDtoList))
                .build();

        orderRepo.save(newOrder);
        logger.info("Order generated and saved");

        logger.info("Generating Bill ...");

        BillEntity billEntity = BillEntity.builder()
                .id(UUID.randomUUID().toString())
                .orderId(newOrder.getId())
                .userId(userId)
                .productsList(productEntities)
                .amount(newOrder.getTotal())
                .paymentId(null)
                .idempotencyKey(null)
                .paymentIntent(null)
                .status(Status.DRAFT)
                .updatedAt(null)
                .build();

        BillEntity savedBill =billRepo.save(billEntity);
        logger.info("bill generated and saved");


return BillMapper.toDto(savedBill);

    }

    @Transactional
    public BillResDto placeOrder(String key,String userId,String orderId) throws NullPointerException{
        try {
            logger.info("checking for idempoKey ....");
            IdempotencyRecord record = idempotencyService.checkIdempoKey(userId, key);

            if (record != null) {
                logger.info("existing idempoKey found ...");
                BillEntity entity = billRepo.findByOrderId(record.getOrderId());
                return BillMapper.toDto(entity);
            }
            logger.info("adding idempoKey to bill....");
            BillEntity entity = billRepo.findByOrderId(orderId);

            entity = BillEntity.builder()
                    .id(entity.getId())
                    .orderId(entity.getOrderId())
                    .productsList(entity.getProductsList())
                    .userId(entity.getUserId())
                    .amount(entity.getAmount())
                    .paymentId(entity.getPaymentId())
                    .idempotencyKey(key)
                    .paymentIntent(entity.getPaymentIntent())
                    .status(Status.CREATED)
                    .issuedAt(entity.getIssuedAt())
                    .updatedAt(Instant.now())
                    .build();

            logger.info("products getting updated in stock...");
            entity.getProductsList().forEach(
                    product -> productService.updateProduct(false,product.getStock(),product)
            );

            billRepo.save(entity);
            return BillMapper.toDto(entity);

        }catch (NullPointerException ex){
            throw new NullPointerException("please recreate the order");
        }
    }

}




