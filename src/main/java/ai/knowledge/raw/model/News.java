package ai.knowledge.raw.model;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class News {
    /**
     * title: str
     * publishedAt: str
     * url: str
     * # optional fields
     * urlToImage: Optional[str]
     * description: Optional[str]
     * content: Optional[str]
     * author: Optional[str]
     * articleText: Optional[str]
     * # complex objects
     * source: SourceArticle
     */
    private String title;
    private String publishedAt;
    private String url;
    private Optional<String> urlToImage;
    private Optional<String> description;
    private Optional<String> content;
    private Optional<String> author;
    private Optional<String> articleText;
    private Source source;
}
