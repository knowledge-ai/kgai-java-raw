package ai.knowledge.raw.service.kafka;

import ai.knowledge.news.raw.NewsArticle;
import ai.knowledge.raw.service.orientdb.WriterClientService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

@Service
@CommonsLog(topic = "Raw Consumer Logger")
public class ConsumerService {

    @Value("${orientdb.url}")
    private String orientUrl;

    @Value("${orientdb.db}")
    private String orientDB;

    @Value("${orientdb.writerUser}")
    private String orientWriteUser;

    @Value("${orientdb.writerPass}")
    private String orientWritePass;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    private WriterClientService writerClientService;


    @KafkaListener(id = "RawConsumer", topics = "${topic.name}", autoStartup =
            "false")
    public void consume(ConsumerRecord<String, NewsArticle> record) {
        log.info(String.format("Consumed message -> %s", record.value()));
        writerClientService.createVertex(record);
    }

    // delay start, read: https://docs.spring.io/spring-kafka/reference/html/#kafkalistener-lifecycle
    // delay till object creation as then the class<T> will be known
    public void startConsumer() {
        // initialize the db connection before starting the consumer
        writerClientService = new WriterClientService(orientUrl, orientDB,
                orientWriteUser, orientWritePass);
        this.registry.getListenerContainer("RawConsumer").start();
    }

    public void stopConsumer() {
        this.registry.getListenerContainer("RawConsumer").stop();
    }


}
