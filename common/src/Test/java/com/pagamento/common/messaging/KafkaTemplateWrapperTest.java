package com.pagamento.common.messaging;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

public class KafkaTemplateWrapperTest {

    @Mock
    private KafkaTemplate<String, String> delegate;

    private KafkaTemplateWrapper<String, String> wrapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        wrapper = new KafkaTemplateWrapper<>(delegate);
    }

    @Test
    public void testSendDelegates() {
        SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();
        when(delegate.send("topic1", "data")).thenReturn(future);

        ListenableFuture<?> result = wrapper.send("topic1", "data");
        assertSame(future, result);
        verify(delegate).send("topic1", "data");
    }

    @Test
    public void testSendWithKeyDelegates() {
        SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();
        when(delegate.send("topic2", "key", "payload")).thenReturn(future);

        ListenableFuture<?> result = wrapper.send("topic2", "key", "payload");
        assertSame(future, result);
        verify(delegate).send("topic2", "key", "payload");
    }

    @Test
    public void testSendWithCallbackConverts() throws ExecutionException, InterruptedException {
        SettableListenableFuture<SendResult<String, String>> lf = new SettableListenableFuture<>();
        when(delegate.send("t", "m")).thenReturn(lf);

        ListenableFuture<SendResult<String, String>> result = wrapper.sendWithCallback("t", "m");
        assertFalse(result.isDone());

        ProducerRecord<String, String> record = new ProducerRecord<>("t", "m");
        RecordMetadata metadata = new RecordMetadata(new TopicPartition("t", 0), 0, 0, 0L, 0L, 0, 0);
        SendResult<String, String> sendResult = new SendResult<>(record, metadata);

        lf.set(sendResult);

        assertTrue(result.isDone());
        assertEquals(sendResult, result.get()); // Corrigido: usamos get() em vez de join()
    }

  
 /*   @SuppressWarnings("unchecked")
    @Test
    public void testMetricsDelegates() {
        Map<MetricName, ? extends Metric> metrics = (Map<MetricName, ? extends Metric>) new HashMap<MetricName, Metric>();
        when(delegate.metrics()).thenReturn((Map<MetricName, ? extends Metric>) metrics);

        Map<?, ?> result = wrapper.metrics();
        assertSame(metrics, result);
        verify(delegate).metrics();
    }
   */ 
    
    
    @Test
    public void testPartitionsForDelegates() {
        List<PartitionInfo> list = Collections.singletonList(new PartitionInfo("t", 0, null, null, null));
        when(delegate.partitionsFor("t")).thenReturn(list);

        List<PartitionInfo> result = wrapper.partitionsFor("t");
        assertSame(list, result);
        verify(delegate).partitionsFor("t");
    }

    @Test
    public void testSendOffsetsToTransactionDelegates() {
        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        wrapper.sendOffsetsToTransaction(offsets, "cg");
        verify(delegate).sendOffsetsToTransaction(offsets, "cg");
    }
}
