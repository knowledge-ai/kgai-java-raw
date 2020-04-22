package ai.knowledge.raw.controller;

import ai.knowledge.raw.service.kafka.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RawController {
    @Autowired
    private ConsumerService consumerService;

    public void startConsuming() {
        consumerService.startConsumer();
    }

    public void stopConsuming() {
        consumerService.stopConsumer();
    }
}
