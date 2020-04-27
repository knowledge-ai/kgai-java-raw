package ai.knowledge.raw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
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
    private String urlToImage;
    private String description;
    private String content;
    private String author;
    private String articleText;
    private Source source;
}
