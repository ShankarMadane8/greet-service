package com.example.greetservice.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class GreetScheduler {

    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    @SchedulerLock(name = "GreetScheduler_scheduledTask", lockAtLeastFor = "PT5S", lockAtMostFor = "PT9S")
    public void scheduledTask() {
        log.info("Distributed task executed by this instance at: " + LocalDateTime.now());
    }
}
