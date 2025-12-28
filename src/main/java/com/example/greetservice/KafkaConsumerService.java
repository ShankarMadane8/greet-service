package com.example.greetservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "user-visits", groupId = "greet-group")
    public void consume(String message, 
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("Kafka Event: [Topic={}, Partition={}, Offset={}] -> Payload={}", topic, partition, offset, message);
    }
}
