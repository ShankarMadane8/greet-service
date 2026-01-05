package com.example.greetservice;

import com.example.greetservice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import com.example.greetservice.entity.Student;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final StudentRepository studentRepository;

    @KafkaListener(topics = "user-visits", groupId = "greet-group")
    public void consume(String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("Kafka Event: [Topic={}, Partition={}, Offset={}] -> Payload={}", topic, partition, offset, message);
    }

    @KafkaListener(topics = "student-visits", groupId = "greet-group", containerFactory = "studentKafkaListenerContainerFactory")
    public void consumeStudent(
            Student student,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Kafka Event: [Topic={}, Partition={}, Offset={}] -> Payload={}", topic, partition, offset, student);
        studentRepository.save(student);
        log.info("Saved Student to DB: {}", student);

    }
}
