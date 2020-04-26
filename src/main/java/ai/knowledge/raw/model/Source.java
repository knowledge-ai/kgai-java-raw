package ai.knowledge.raw.model;

import lombok.AllArgsConstructor;

import java.util.Optional;


@AllArgsConstructor
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
    private Optional<String> id;
    private Optional<String> description;
    private Optional<String> url;
    private Optional<String> category;
    private Optional<String> language;
    private Optional<String> country;
}
