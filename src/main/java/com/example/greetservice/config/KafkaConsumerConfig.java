package com.example.greetservice.config;

import java.util.HashMap;
import java.util.Map;

import com.example.greetservice.entity.Student;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

        @Value("${spring.kafka.bootstrap-servers}")
        private String bootstrapServers ;
        @Bean
        public ConsumerFactory<String, Student> studentConsumerFactory() {

                JsonDeserializer<Student> jsonDeserializer = new JsonDeserializer<>(Student.class);

                // 🔑 CRITICAL FIX
                jsonDeserializer.addTrustedPackages("*");
                jsonDeserializer.setUseTypeHeaders(false); // <-- IGNORE producer class
                jsonDeserializer.setRemoveTypeHeaders(true);

                Map<String, Object> props = new HashMap<>();
                props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
                props.put(ConsumerConfig.GROUP_ID_CONFIG, "greet-group");

                // 🔑 ErrorHandlingDeserializer
                props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
                props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

                props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
                props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

                return new DefaultKafkaConsumerFactory<>(
                                props,
                                new StringDeserializer(),
                                jsonDeserializer);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, Student> studentKafkaListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, Student> factory = new ConcurrentKafkaListenerContainerFactory<>();

                factory.setConsumerFactory(studentConsumerFactory());

                // 🔑 Prevent infinite crash loop
                factory.setCommonErrorHandler(
                                new DefaultErrorHandler(
                                                new FixedBackOff(1000L, 3) // retry 3 times then skip
                                ));

                return factory;
        }
}
