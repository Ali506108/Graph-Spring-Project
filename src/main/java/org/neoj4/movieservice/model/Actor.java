package org.neoj4.movieservice.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Node
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Actor {


    @Id @GeneratedValue
    private Long id;
    private String FullName;
    private int age;

    @Relationship(type = "ACTED_IN" , direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private HashSet<ActedIn> actedIns = new HashSet<>();


}
