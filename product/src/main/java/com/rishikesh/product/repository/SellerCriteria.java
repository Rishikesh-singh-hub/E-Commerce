package com.rishikesh.product.repository;

import com.rishikesh.product.entity.SellerEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class SellerCriteria {

    private static MongoTemplate mongoTemplate;

    public SellerCriteria(MongoTemplate mongoTemplate) {
        SellerCriteria.mongoTemplate = mongoTemplate;
    }


    public static String findSellerIdByUserId(String userId) {

        Query query = new Query();

        query.addCriteria(Criteria.where("userId").is(userId));

        SellerEntity seller = mongoTemplate.findOne(query, SellerEntity.class);

        return seller != null ? seller.getId() : null;
    }

}
