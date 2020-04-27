package ai.knowledge.raw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Builder
@Getter
@Setter
public class Source {
    /**
     * name: str
     * # Optional fields
     * id: Optional[str]
     * description: Optional[str]
     * url: Optional[str]
     * category: Optional[str]
     * language: Optional[str]
     * country: Optional[str]
     */
    private String name;
    private String id;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;
}
