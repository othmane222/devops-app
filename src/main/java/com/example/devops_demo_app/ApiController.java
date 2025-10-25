package com.example.devops_demo_app;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
//@RequiredArgsConstructor 
public class ApiController {

    private final MetricsService metricsService;
    
    private final ConcurrentLinkedQueue<String> processingQueue = new ConcurrentLinkedQueue<>();

    public ApiController(MetricsService metricsService, MeterRegistry registry) {
        this.metricsService = metricsService;
        registry.gauge("processing_queue_size", processingQueue, ConcurrentLinkedQueue::size);
    }

    @GetMapping("/order")
    public ResponseEntity<String> createOrder() {
        metricsService.processNewOrder();
        return ResponseEntity.ok("Order created and processed!");
    }

    @GetMapping("/queue/add")
    public ResponseEntity<String> addToQueue() {
        String itemId = "item-" + System.currentTimeMillis();
        processingQueue.add(itemId);
        return ResponseEntity.ok("Added '" + itemId + "' to queue. Current size: " + processingQueue.size());
    }

    @GetMapping("/queue/process")
    public ResponseEntity<String> processFromQueue() {
        String itemId = processingQueue.poll();
        if (itemId != null) {
            return ResponseEntity.ok("Processed '" + itemId + "'. Current size: " + processingQueue.size());
        } else {
            return ResponseEntity.status(404).body("Queue is empty.");
        }
    }
}
