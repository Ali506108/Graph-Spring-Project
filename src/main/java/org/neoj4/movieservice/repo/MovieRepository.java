package org.neoj4.movieservice.repo;

import org.neoj4.movieservice.model.Movie;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {
}
