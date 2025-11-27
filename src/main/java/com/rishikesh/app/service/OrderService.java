package com.rishikesh.app.service;

import com.rishikesh.app.dto.bill.BillResDto;
import com.rishikesh.app.dto.order.OrderDto;
import com.rishikesh.app.dto.order.OrderItemReqDto;
import com.rishikesh.app.dto.order.OrderReqDto;
import com.rishikesh.app.dto.order.OrderTotalResult;
import com.rishikesh.app.entity.*;
import com.rishikesh.app.mapper.BillMapper;
import com.rishikesh.app.mapper.OrderMapper;
import com.rishikesh.app.repository.BillRepo;
import com.rishikesh.app.repository.IdempotencyRepo;
import com.rishikesh.app.repository.OrderRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    private final IdempotencyRepo idempotencyRepo;
    private final OrderRepo orderRepo;
    private final ProductService productService;

    public OrderService(BillRepo billRepo, IdempotencyService idempotencyService, IdempotencyRepo idempotencyRepo, OrderRepo orderRepo, ProductService productService){
        this.billRepo = billRepo;

        this.idempotencyService = idempotencyService;
        this.idempotencyRepo = idempotencyRepo;
        this.orderRepo = orderRepo;
        this.productService = productService;
    }

//    public OrderDto createOrder(String userId, OrderReqDto orderReq) {
//
//        IdempotencyRecord record =idempotencyService.checkIdempoKey(userId, orderReq.getIdempotencyKey());
//
//        if (record!=null){
//            logger.info("looking for previous order...");
//            OrderEntity order =  orderRepo.findById(record.getOrderId()).orElse(null);
//            logger.info("order found:  {}",order.getIdempotencyKey());
//            return OrderMapper.toOrderDto(order);
//        }
//        List<OrderItemReqDto> orderItemsDtoList = orderReq.getItems();
//
//       List<ProductEntity> productEntities =orderReq.getItems().stream().map(
//               productService::validateProduct
//       ).toList();
//
//
//
//    OrderTotalResult totalResult = total(productEntities,orderItemsDtoList);
//    List<OrderItem> orderItems = totalResult.getUpdatedProducts().stream()
//            .map(OrderMapper::toOrderItem)
//            .toList();
//
//    OrderEntity newOrder = OrderEntity.builder()
//                .id(UUID.randomUUID().toString())
//                .userId(userId)
//                .idempotencyKey(orderReq.getIdempotencyKey())
//            .items(orderItems)
//            .total(totalResult.getTotal())
//            .build();
//
//        IdempotencyRecord newRecord = IdempotencyRecord.builder()
//                        .id(UUID.randomUUID().toString())
//                        .userId(newOrder.getUserId())
//                        .idempotencyKey(newOrder.getIdempotencyKey())
//                        .orderId(newOrder.getId())
//                        .status(newOrder.getStatus())
//                        .createdAt(newOrder.getCreatedAt())
//                        .updatedAt(newOrder.getUpdatedAt())
//                        .build();
//        idempotencyRepo.save(newRecord);
//        orderRepo.save(newOrder);
//
//        return OrderMapper.toOrderDto(newOrder);
//
//    }

    private OrderTotalResult total(List<ProductEntity> productEntities,
                                   List<OrderItemReqDto> orderItemsDtoList) {

        // Map: productId → ProductEntity
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

            // compute line total
            BigDecimal line = orig.getPrice().multiply(BigDecimal.valueOf(qty));
            total = total.add(line);

            // create a copy and set stock = qty
            ProductEntity copy = new ProductEntity();
            copy.setId(orig.getId());
            copy.setName(orig.getName());
            copy.setDescription(orig.getDescription());
            copy.setPrice(orig.getPrice());
            copy.setStock(qty);         // this is your "qty field"

            updatedProducts.add(copy);
        }
        return new OrderTotalResult(updatedProducts, total);
    }

    public BillResDto generateBill(String userId,OrderReqDto orderRequest){

        logger.info("Checking and removing if any previous draft with user is found....");

        billRepo.deleteByIdAndStatus(userId,Status.DRAFT);
        orderRepo.deleteByUserIdAndStatus(userId,Status.DRAFT);
        logger.info("generating Bill request received...");
        List<OrderItemReqDto> orderItemsDtoList = orderRequest.getItems();

        List<ProductEntity> productEntities =orderRequest.getItems().stream().map(
                productService::validateProduct
        ).toList();

        logger.info("Validated Products from stocks ...");

        OrderTotalResult totalResult = total(productEntities,orderItemsDtoList);
        List<OrderItem> orderItems = totalResult.getUpdatedProducts().stream()
                .map(OrderMapper::toOrderItem)
                .toList();

        OrderEntity newOrder = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .idempotencyKey(null)
                .items(orderItems)
                .status(Status.DRAFT)
                .total(totalResult.getTotal())
                .build();

        orderRepo.save(newOrder);
        logger.info("Order generated and saved");

        logger.info("Generating Bill ...");

        BillEntity billEntity = BillEntity.builder()
                .id(UUID.randomUUID().toString())
                .orderId(newOrder.getId())
                .userId(userId)
                .productsList(totalResult.getUpdatedProducts())
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
//        return OrderMapper.toOrderDto(newOrder);

    }

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

            billRepo.save(entity);
            return BillMapper.toDto(entity);

        }catch (NullPointerException ex){
            throw new NullPointerException("please recreate the order");
        }
    }

}




