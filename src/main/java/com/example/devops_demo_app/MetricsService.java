package com.example.devops_demo_app;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class MetricsService {

    private final Counter orderCounter;
    private final DistributionSummary orderAmountSummary;
    private final Timer orderProcessingTimer;

    public MetricsService(MeterRegistry registry) {
        this.orderCounter = Counter.builder("orders_total")
                .description("Total number of orders placed.")
                .tag("type", "online") 
                .register(registry);

        this.orderAmountSummary = DistributionSummary.builder("orders_amount_summary")
                .description("Summary of order amounts.")
                .baseUnit("euros") 
                .register(registry);
        
        this.orderProcessingTimer = Timer.builder("orders_processing_time")
                .description("Measures the time taken to process an order.")
                .register(registry);
    }

    
    public void processNewOrder() {
        Timer.Sample sample = Timer.start();

        try {
            orderCounter.increment();

            double amount = 10 + new Random().nextDouble() * 190;
            orderAmountSummary.record(amount);

            Thread.sleep(new Random().nextInt(500));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            sample.stop(orderProcessingTimer);
        }
    }
}
