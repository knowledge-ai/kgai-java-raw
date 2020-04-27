package ai.knowledge.raw.service.kafka;

import ai.knowledge.news.raw.NewsArticle;
import ai.knowledge.news.raw.SourceArticle;
import ai.knowledge.raw.model.News;
import ai.knowledge.raw.model.Source;
import ai.knowledge.raw.service.orientdb.WriterClientService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import java.util.Optional;

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


    @KafkaListener(id = "Java-RawConsumer", topics = "${topic.name}",
            autoStartup = "false")
    public void consume(ConsumerRecord<String, NewsArticle> record) {
        log.info(String.format("Consumed message -> %s", record.value()));
        // each record is ConsumerRecord<String, NewsArticle>
        writerClientService.createVertex(mapToNews(record.value()), record.key());

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

    public News mapToNews(NewsArticle newsArticle) {
        SourceArticle sourceArticle = newsArticle.getSource();
        return News.builder()
                .title(newsArticle.getTitle().toString())
                .publishedAt(newsArticle.getPublishedAt().toString())
                .url(newsArticle.getUrl().toString())
                .urlToImage(Optional.ofNullable(newsArticle.getUrlToImage()).toString())
                .description(Optional.ofNullable(newsArticle.getDescription()).toString())
                .content(Optional.ofNullable(newsArticle.getContent()).toString())
                .author(Optional.ofNullable(newsArticle.getAuthor()).toString())
                .articleText(Optional.ofNullable(newsArticle.getArticleText()).toString())
                .source(Source.builder()
                        .name(sourceArticle.getName().toString())
                        .id(Optional.ofNullable(sourceArticle.getId()).toString())
                        .description(Optional.ofNullable(sourceArticle.getDescription()).toString())
                        .url(Optional.ofNullable(sourceArticle.getUrl()).toString())
                        .category(Optional.ofNullable(sourceArticle.getCategory()).toString())
                        .language(Optional.ofNullable(sourceArticle.getLanguage()).toString())
                        .country(Optional.ofNullable(sourceArticle.getCountry()).toString())
                        .build())
                .build();
    }


}
