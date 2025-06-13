// KafkaTemplateWrapperTest.java
package com.pagamento.common.messaging;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        SettableListenableFuture future = new SettableListenableFuture();
        when(delegate.send("topic1", "data")).thenReturn(future);

        ListenableFuture<?> result = wrapper.send("topic1", "data");
        assertSame(future, result);
        verify(delegate).send("topic1", "data");
    }

    @Test
    public void testSendWithKeyDelegates() {
        SettableListenableFuture future = new SettableListenableFuture();
        when(delegate.send("topic2", "key", "payload")).thenReturn(future);

        ListenableFuture<?> result = wrapper.send("topic2", "key", "payload");
        assertSame(future, result);
        verify(delegate).send("topic2", "key", "payload");
    }

    @Test
    public void testSendWithCallbackConverts() {
        SettableListenableFuture<SendResult<String,String>> lf = new SettableListenableFuture<>();
        wrapper = new KafkaTemplateWrapper<>(delegate);
        when(delegate.send("t", "m")).thenReturn(lf);

        CompletableFuture<?> cf = wrapper.sendWithCallback("t", "m");
        assertFalse(cf.isDone());

        SendResult<String,String> sendResult = new SendResult<>(new ProducerRecord<>("t","m"), null);
        lf.set(sendResult);
        assertTrue(cf.isDone());
        assertEquals(sendResult, cf.join());
    }

    @Test
    public void testMetricsDelegates() {
        Map<org.apache.kafka.common.MetricName, org.apache.kafka.common.Metric> metrics = Collections.emptyMap();
        when(delegate.metrics()).thenReturn(metrics);

        Map<?,?> result = wrapper.metrics();
        assertSame(metrics, result);
        verify(delegate).metrics();
    }

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