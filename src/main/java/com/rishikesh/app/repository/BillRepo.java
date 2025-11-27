package com.rishikesh.app.repository;

import com.rishikesh.app.entity.BillEntity;
import com.rishikesh.app.entity.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BillRepo extends MongoRepository<BillEntity,String> {
    public BillEntity findByOrderId (String orderId);
    void deleteByIdAndStatus(String id, Status status);


}
