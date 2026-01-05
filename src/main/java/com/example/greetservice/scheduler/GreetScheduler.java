package com.example.greetservice.scheduler;

import com.example.greetservice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class GreetScheduler {

    private final StudentRepository studentRepository;

    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    @SchedulerLock(name = "GreetScheduler_scheduledTask", lockAtLeastFor = "PT5S", lockAtMostFor = "PT9S")
    public void scheduledTask() {
        log.info("Distributed task executed by this instance at: " + LocalDateTime.now());


       Pageable pageRequest = PageRequest.of(0, 2);
        var pendingStudents = studentRepository.findByStatus("PENDING", pageRequest);

        if (pendingStudents.isEmpty()) {
            log.info("No PENDING students found.");
            return;
        }

        // 2. Process them
        pendingStudents.forEach(student -> {
            log.info("Batch Processing student: {}", student);
            student.setStatus("PROCESSED");
        });

        // 3. Save updates
        studentRepository.saveAll(pendingStudents);
        log.info("Processed and updated {} students in this batch.", pendingStudents.size());
    }
}
