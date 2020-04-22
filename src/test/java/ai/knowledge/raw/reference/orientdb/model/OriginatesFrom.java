package ai.knowledge.raw.reference.orientdb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// create an edge from a class: source <--[originatesFrom]-- news
@Getter
@Setter
@AllArgsConstructor
public class OriginatesFrom {
    public Float confidence;
    public Boolean isPredicted;
}
