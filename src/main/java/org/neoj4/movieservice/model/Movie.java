package org.neoj4.movieservice.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@Node
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String description;


}
