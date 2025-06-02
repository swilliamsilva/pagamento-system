package com.pagamento.boleto.repository.postgres;

import com.pagamento.boleto.model.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto, Long> {
    // Query methods para boletos
    List<Boleto> findByDueDateBefore(Date date);
}
