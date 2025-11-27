package com.rishikesh.app.service;

import com.rishikesh.app.entity.IdempotencyRecord;
import com.rishikesh.app.repository.IdempotencyRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IdempotencyService {
    Logger logger = LoggerFactory.getLogger(IdempotencyService.class);
    private final IdempotencyRepo idempotencyRepo;

    public IdempotencyService( IdempotencyRepo idempotencyRepo) {
        this.idempotencyRepo = idempotencyRepo;
    }

    public IdempotencyRecord checkIdempoKey(String userId,String idempotencyKey) {

        IdempotencyRecord record = idempotencyRepo.findByUserIdAndIdempotencyKey(userId,idempotencyKey).orElse(null);

        if (record!=null){
            logger.info("order already exist");
            return record;
        }
        logger.info("order not found creating new: {}",record);
        return record;
    }
}
