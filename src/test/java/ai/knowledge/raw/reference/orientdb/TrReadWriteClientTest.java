package ai.knowledge.raw.reference.orientdb;

import ai.knowledge.news.raw.NewsArticle;
import ai.knowledge.news.raw.SourceArticle;
import ai.knowledge.raw.reference.orientdb.model.OriginatesFrom;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class TrReadWriteClientTest {

    @Value("${orientdb.url}")
    private String orientUrl;

    @Value("${orientdb.db}")
    private String orientDB;

    @Value("${orientdb.writerUser}")
    private String orientWriteUser;

    @Value("${orientdb.writerPass}")
    private String orientWritePass;

    @Value("${orientdb.readerUser}")
    private String orientReadUser;

    @Value("${orientdb.readerPass}")
    private String orientReadPass;

    private TrWriterClient trWriterClient;

    private TrReaderClient trReaderClient;

    @Test
    void vertexCreationTest() {
        // create the writer client
        trWriterClient = new TrWriterClient(orientUrl, orientDB,
                orientWriteUser, orientWritePass);

        // create the reader client
        trReaderClient = new TrReaderClient(orientUrl, orientDB,
                orientReadUser, orientReadPass);
        // create the Vertex object and append a complex object to check if
        // can be handled
        String article_title = "title";
        NewsArticle newsArticle = new NewsArticle(article_title, "date", "url",
                "url2image", "some description", "some content", "Author " +
                "Name", "",
                new SourceArticle("name", "id", "description", "url",
                        "category", "language", "country"));
        // create the vertex in the DB
        trWriterClient.createVertex(newsArticle);

        // create a sourceArticle Vertex
        String source_name = "name";
        SourceArticle sourceArticle = new SourceArticle(source_name, "id",
                "description", "url",
                "category", "language", "country");

        // create the vertex in the DB
        trWriterClient.createVertex(sourceArticle);

        // read from newsArticle and SourceArticle
        ArrayList<OResult> results_news =
                trReaderClient.executeQuery(String.format(
                        "Select * from %s limit 1", newsArticle.getClass().getSimpleName()));

        ArrayList<OResult> results_source =
                trReaderClient.executeQuery(String.format(
                        "Select * from %s limit 1", sourceArticle.getClass().getSimpleName()));

        assert results_news.size() != 0 : "Expected results of newsArticle " +
                "search";
        assert results_source.size() != 0 : "Expected results of sourceArticle " +
                "search";

        assert results_news.get(0).getProperty("title").toString().equalsIgnoreCase(article_title) :
                "Expected the tile of the news article to be same as test data";
        assert results_source.get(0).getProperty("name").toString().equalsIgnoreCase(source_name) :
                "Expected the name of the source to be same as test data";

    }

    @Test
    void graphCreationTest() {
        // create the writer client
        trWriterClient = new TrWriterClient(orientUrl, orientDB,
                orientWriteUser, orientWritePass);

        // create the reader client
        trReaderClient = new TrReaderClient(orientUrl, orientDB,
                orientReadUser, orientReadPass);
        // create the Vertex object and append a complex object to check if
        // can be handled
        String article_title = "title";
        NewsArticle newsArticle = new NewsArticle(article_title, "date", "url",
                "url2image", "some description", "some content", "Author " +
                "Name", "",
                new SourceArticle("name", "id", "description", "url",
                        "category", "language", "country"));
        // create the vertex in the DB
        OVertex newsVertex = trWriterClient.createVertex(newsArticle);

        // create a sourceArticle Vertex
        String source_name = "name";
        SourceArticle sourceArticle = new SourceArticle(source_name, "id",
                "description", "url",
                "category", "language", "country");

        // create the vertex in the DB
        OVertex sourceVertex = trWriterClient.createVertex(sourceArticle);

        // create the edge in the DB: source --[produced]--> news
        OEdge producedEdge = trWriterClient.createEdge(sourceVertex,
                newsVertex, "produced");

        // create an edge from a class: source <--[originatesFrom]-- news
        OEdge originatesEdge = trWriterClient.createEdge(sourceVertex,
                newsVertex, new OriginatesFrom((float) 0.93, true));


        // read from newsArticle and SourceArticle
        ArrayList<OResult> results_news =
                trReaderClient.executeQuery(String.format(
                        "Select * from %s limit 1", newsArticle.getClass().getSimpleName()));

        ArrayList<OResult> results_source =
                trReaderClient.executeQuery(String.format(
                        "Select * from %s limit 1", sourceArticle.getClass().getSimpleName()));

        // select from produced and origanatedFrom
        ArrayList<OResult> results_produced =
                trReaderClient.executeQuery(String.format(
                        "Select * from %s limit 1", "produced"));

        ArrayList<OResult> results_originatedFrom =
                trReaderClient.executeQuery(String.format(
                        "Select * from %s limit 1",
                        OriginatesFrom.class.getSimpleName()));

        // assert basics of Vertex
        assert results_news.size() != 0 : "Expected results of newsArticle " +
                "search";
        assert results_source.size() != 0 : "Expected results of sourceArticle " +
                "search";

        // assert basics of Edge
        assert results_produced.size() != 0 : "Expected results of produced " +
                "search";
        assert results_originatedFrom.size() != 0 : "Expected results of " +
                "originatedFrom search";

        Assert.assertEquals("Expected edge document property to match",
                results_originatedFrom.get(0).getProperty(
                        "confidence"), (float) 0.93, (float) 0.00);


    }

}
