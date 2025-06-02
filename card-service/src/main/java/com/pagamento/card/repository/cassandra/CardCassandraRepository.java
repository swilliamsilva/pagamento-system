package com.pagamento.card.repository.cassandra;

import com.pagamento.card.model.CardTransaction;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardCassandraRepository extends CassandraRepository<CardTransaction, String> {
    // Consultas específicas para cartões
    List<CardTransaction> findByCardNumber(String cardNumber);
}
