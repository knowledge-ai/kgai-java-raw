package ai.knowledge.raw.reference.kafka.consumer;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

@Service
@CommonsLog(topic = "TR Consumer Logger")
public class TrConsumer<T> {

    @Autowired
    private KafkaListenerEndpointRegistry registry;


    @KafkaListener(id = "TrConsumer", topics = "${topic.name}", autoStartup =
            "false")
    public void consume(ConsumerRecord<String, Class<T>> record) {
        log.info(String.format("Consumed message -> %s", record.value()));
    }

    // delay start, read: https://docs.spring.io/spring-kafka/reference/html/#kafkalistener-lifecycle
    // delay till object creation as then the class<T> will be known
    public void startConsumer() {
        this.registry.getListenerContainer("TrConsumer").start();
    }

    public void stopConsumer() {
        this.registry.getListenerContainer("TrConsumer").stop();
    }


}
