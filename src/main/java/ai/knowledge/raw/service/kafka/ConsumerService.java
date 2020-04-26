package ai.knowledge.raw.service.kafka;

import ai.knowledge.news.raw.NewsArticle;
import ai.knowledge.raw.model.News;
import ai.knowledge.raw.service.orientdb.WriterClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    private ObjectMapper objectMapper = new ObjectMapper();

    private WriterClientService writerClientService;


    @KafkaListener(id = "Java-RawConsumer", topics = "${topic.name}",
            autoStartup = "false")
    public void consume(ConsumerRecord<String, NewsArticle> record) {
        log.info(String.format("Consumed message -> %s", record.value()));
        // each record is ConsumerRecord<String, NewsArticle>
        // needs explicit cast/variable otherwise library below not works
        News news = mapToNews(record.value().toString());
        if (news != null) {
            writerClientService.createVertex(news, record.key());
        }
    }

    // delay start, read: https://docs.spring.io/spring-kafka/reference/html/#kafkalistener-lifecycle
    // delay till object creation as then the class<T> will be known
    public void startConsumer() {
        // initialize the db connection before starting the consumer
        writerClientService = new WriterClientService(orientUrl, orientDB,
                orientWriteUser, orientWritePass);
        this.registry.getListenerContainer("Java-RawConsumer").start();
    }

    public void stopConsumer() {
        this.registry.getListenerContainer("Java-RawConsumer").stop();
    }

    private News mapToNews(String jsonString) {
        //Car car = objectMapper.readValue(json, Car.class);
        News news = null;
        try {
            news = objectMapper.readValue(jsonString, News.class);
        } catch (IOException e) {
            log.error(String.format("Could not convert Kafka message to News " +
                    "data type, jsonString: %s", jsonString));
        }
        return news;
    }


}
