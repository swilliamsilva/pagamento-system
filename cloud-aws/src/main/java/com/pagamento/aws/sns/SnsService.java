package com.pagamento.aws.sns;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class SnsService {

    private final SnsClient snsClient;
    
    public SnsService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public String publishMessage(String topicArn, String message) {
        return publishMessage(topicArn, message, null);
    }

    public String publishMessage(String topicArn, String message, Map<String, MessageAttributeValue> attributes) {
        PublishRequest.Builder builder = PublishRequest.builder()
            .topicArn(topicArn)
            .message(message);
        
        if (attributes != null) {
            builder.messageAttributes(attributes);
        }
        
        PublishResponse response = snsClient.publish(builder.build());
        return response.messageId();
    }

    public String createTopic(String topicName) {
        CreateTopicResponse response = snsClient.createTopic(CreateTopicRequest.builder()
            .name(topicName)
            .build());
        
        return response.topicArn();
    }

    public void subscribeToTopic(String topicArn, String protocol, String endpoint) {
        snsClient.subscribe(SubscribeRequest.builder()
            .topicArn(topicArn)
            .protocol(protocol)
            .endpoint(endpoint)
            .build());
    }

    public void setTopicAttributes(String topicArn, Map<String, String> attributes) {
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            snsClient.setTopicAttributes(SetTopicAttributesRequest.builder()
                .topicArn(topicArn)
                .attributeName(entry.getKey())
                .attributeValue(entry.getValue())
                .build());
        }
    }

    public void publishToPhone(String phoneNumber, String message) {
        snsClient.publish(PublishRequest.builder()
            .phoneNumber(phoneNumber)
            .message(message)
            .build());
    }

    public void publishWithSubject(String topicArn, String subject, String message) {
        snsClient.publish(PublishRequest.builder()
            .topicArn(topicArn)
            .subject(subject)
            .message(message)
            .build());
    }
}
