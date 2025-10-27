package org.neoj4.movieservice.repo;

import org.neoj4.movieservice.model.Actor;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActorRepository extends Neo4jRepository<Actor , Long> {
}
