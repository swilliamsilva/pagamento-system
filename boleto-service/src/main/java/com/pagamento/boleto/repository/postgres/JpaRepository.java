package com.pagamento.boleto.repository.postgres;

import java.util.List;
import java.util.Optional;

public interface JpaRepository<T, ID> {
    <S extends T> S save(S entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    boolean existsById(ID id);
    // Adicione outros métodos conforme necessário
}