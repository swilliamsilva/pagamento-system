package com.pagamento.payment.repository;



import com.pagamento.payment.model.PaymentDocument;


import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMongoRepository extends MongoRepository<PaymentDocument, String> {
    List<PaymentDocument> findByStatus(String status);
}

