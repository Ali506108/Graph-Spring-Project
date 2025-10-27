package org.neoj4.movieservice.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActedIn {

    @RelationshipId
    private Long id;
    private String role;
    private int year;

    @TargetNode
    private Movie movie;
}
