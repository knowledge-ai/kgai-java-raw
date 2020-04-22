package ai.knowledge.raw.reference.kafka.producer;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@CommonsLog(topic = "Producer Logger")
public class TrProducer<T> {

    @Value("${topic.name}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplate;

    public void sendMessage(T message) {
        this.kafkaTemplate.send(this.topic, message.toString(), message);
        log.info(String.format("Produced news article -> %s", message));
    }
}
