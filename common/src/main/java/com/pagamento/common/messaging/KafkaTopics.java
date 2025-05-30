package com.pagamento.common.messaging;

public class KafkaTopics {

    // Tópicos de pagamento
    public static final String PAYMENT_CREATED = "payment.created";
    public static final String PAYMENT_PROCESSED = "payment.processed";
    public static final String PAYMENT_FAILED = "payment.failed";
    
    // Tópicos de reconciliação
    public static final String RECONCILIATION_REQUEST = "reconciliation.request";
    public static final String RECONCILIATION_RESPONSE = "reconciliation.response";
    
    // Tópicos de notificação
    public static final String NOTIFICATION_PAYMENT_SUCCESS = "notification.payment.success";
    public static final String NOTIFICATION_PAYMENT_FAILURE = "notification.payment.failure";
    
    // Tópicos de antifraude
    public static final String ANTIFRAUD_ANALYSIS_REQUEST = "antifraud.analysis.request";
    public static final String ANTIFRAUD_ANALYSIS_RESPONSE = "antifraud.analysis.response";
    
    // Tópicos de cancelamento
    public static final String PAYMENT_CANCELLATION_REQUEST = "payment.cancellation.request";
    public static final String PAYMENT_CANCELLATION_CONFIRMED = "payment.cancellation.confirmed";
    
    // Tópicos de relatórios
    public static final String REPORT_GENERATION_REQUEST = "report.generation.request";
    public static final String REPORT_GENERATED = "report.generated";
    
    // Tópicos de integração com ERP
    public static final String ERP_PAYMENT_SYNC = "erp.payment.sync";
}
