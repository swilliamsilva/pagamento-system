package com.pagamento.payment.repository.mongo;

import com.pagamento.payment.model.PaymentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMongoRepository extends MongoRepository<PaymentDocument, String> {
    // Métodos customizados para pagamentos
    List<PaymentDocument> findByStatus(String status);
}
