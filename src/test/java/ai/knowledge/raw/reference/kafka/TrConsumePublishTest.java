package ai.knowledge.raw.reference.kafka;

import ai.knowledge.raw.reference.kafka.consumer.TrConsumer;
import ai.knowledge.raw.reference.kafka.producer.TrProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import ai.knowledge.news.raw.NewsArticle;
import ai.knowledge.news.raw.SourceArticle;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
public class TrConsumePublishTest {
    @Autowired
    private TrTopicCreator trTopicCreator;

    @Autowired
    private TrConsumer<NewsArticle> trConsumer;

    @Autowired
    private TrProducer<NewsArticle> trProducer;

    @Test
    public void publishConsumeTest(CapturedOutput output) throws InterruptedException {
        // create a sourceArticle
        String source_name = "name";
        SourceArticle sourceArticle = new SourceArticle(source_name, "id",
                "description", "url",
                "category", "language", "country");
        String article_title = "title";
        NewsArticle newsArticle = new NewsArticle(article_title, "date", "url",
                "url2image", "some description", "some content", "Author " +
                "Name", "",
                sourceArticle);

        // publish message
        trProducer.sendMessage(newsArticle);

        // consume message
        trConsumer.startConsumer();

        // wait for consumer to consume message
        Thread.sleep(2000);

        // assert part of consumed
        assertThat(output).contains("\"title\", \"publishedAt\": \"date\"");

        trConsumer.stopConsumer();

    }

}
