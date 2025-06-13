package com.pagamento.common.messaging;

import org.apache.kafka.clients.producer.Producer;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Objects;

/**
 * Gerenciador de transações para operações Kafka.
 * 
 * @deprecated Use {@link org.springframework.kafka.transaction.KafkaTransactionManager} do Spring Kafka
 */
@Deprecated
public class KafkaTransactionManager<K, V> extends AbstractPlatformTransactionManager {

    private final ProducerFactory<K, V> producerFactory;

    public KafkaTransactionManager(ProducerFactory<K, V> producerFactory) {
        Objects.requireNonNull(producerFactory, "ProducerFactory não pode ser nulo");
        this.producerFactory = producerFactory;
        setNestedTransactionAllowed(false);
        setValidateExistingTransaction(false);
    }

    @Override
    protected Object doGetTransaction() {
        KafkaTransactionObject txObject = new KafkaTransactionObject();
        Producer<K, V> producer = (Producer<K, V>) TransactionSynchronizationManager.getResource(producerFactory);
        txObject.setProducer(producer);
        return txObject;
    }

    @Override
    protected void doBegin(Object transaction, Object definition) {
        KafkaTransactionObject txObject = (KafkaTransactionObject) transaction;
        
        if (txObject.getProducer() == null) {
            Producer<K, V> producer = this.producerFactory.createProducer();
            txObject.setProducer(producer);
            TransactionSynchronizationManager.bindResource(producerFactory, producer);
        }
        
        txObject.getProducer().beginTransaction();
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        KafkaTransactionObject txObject = (KafkaTransactionObject) status.getTransaction();
        try {
            txObject.getProducer().commitTransaction();
        } finally {
            TransactionSynchronizationManager.unbindResource(producerFactory);
            txObject.getProducer().close();
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        KafkaTransactionObject txObject = (KafkaTransactionObject) status.getTransaction();
        try {
            txObject.getProducer().abortTransaction();
        } finally {
            TransactionSynchronizationManager.unbindResource(producerFactory);
            txObject.getProducer().close();
        }
    }

    private static class KafkaTransactionObject {
        private Producer<?, ?> producer;

        public Producer<?, ?> getProducer() {
            return producer;
        }

        public void setProducer(Producer<?, ?> producer) {
            this.producer = producer;
        }
    }
}