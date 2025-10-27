package org.neoj4.movieservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neoj4.movieservice.model.ActedIn;
import org.neoj4.movieservice.model.Actor;
import org.neoj4.movieservice.model.Movie;
import org.neoj4.movieservice.repo.ActorRepository;
import org.neoj4.movieservice.repo.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieActorService {

    private final MovieRepository repository;
    private final ActorRepository actorRepository;
    private final Executor movieExecutor;




    public CompletableFuture<Movie> createMovie(Movie movie ){
        return CompletableFuture.supplyAsync(() -> repository.save(movie) , movieExecutor)
                .thenApply(movies -> {
                    log.info("Created movie: {}", movies);
                    return movies;
                }).exceptionally(ex -> {
                    log.error("Error while creating movie", ex);
                    throw new CompletionException(ex);
                });
    }

    public CompletableFuture<Actor> createActor(Actor actor){
        return CompletableFuture.supplyAsync(() -> actorRepository.save(actor) , movieExecutor)
                .thenApply(mov -> {
                    log.info("Created actor: {}", mov);
                    return mov;
                })
                .exceptionally(ex -> {
                    log.error("Error creating actor", ex);
                    throw new CompletionException(ex);
                });
    }


    public CompletableFuture<Void> linkActorToMovie(Long movieId , Long actorId ,
                                                    String role , int year) {

        CompletableFuture<Actor> actorFuture = CompletableFuture.supplyAsync(() ->
                        actorRepository.findById(actorId)
                                .orElseThrow(() -> new RuntimeException("Actor not found")),
                movieExecutor);

        CompletableFuture<Movie> movieFuture = CompletableFuture.supplyAsync(() ->
                repository.findById(movieId)
                        .orElseThrow(() -> new RuntimeException("Movie not founded"))
        );

        return actorFuture.thenAcceptBoth(movieFuture ,(actor , movie) -> {
            ActedIn actorActedIn = ActedIn.builder()
                    .role(role)
                    .year(year)
                    .movie(movie)
                    .build();
            actor.getActedIns().add(actorActedIn);
            actorRepository.save(actor);

            log.info("Linked actors to movies: {}", actor.getActedIns());

        }).exceptionally(ex -> {
            log.error("Error linking actors to movies", ex);
            throw new CompletionException(ex);
        });
    }





    public CompletableFuture<Movie> updateMovie(Movie movie , Long id) {
        return CompletableFuture.supplyAsync(() -> repository.findById(id).orElseThrow() ,  movieExecutor)
                .thenApply(mov -> repository.save(movie))
                .thenApply(mov -> {
                    log.info("Updated movie: {}", mov);
                    return mov;
                })
                .exceptionally(ex ->{
                    log.error("Error updating movie", ex);
                    throw new CompletionException(ex);
                });
    }


    public CompletableFuture<Movie> getMovieById(Long id) {
        return CompletableFuture.supplyAsync(() -> repository.findById(id).orElseThrow() , movieExecutor)
                .thenApply(mov -> {
                    log.info("Retrieved movie: {}", mov);
                    return mov;
                }).
                exceptionally(ex -> {
                    log.error("Error retrieving movie", ex);
                    throw new CompletionException(ex);
                });
    }

    public CompletableFuture<Actor> getActorById(Long id) {
        return CompletableFuture.supplyAsync(() -> actorRepository.findById(id).orElseThrow()
                , movieExecutor);
    }



    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }


}