package org.neoj4.movieservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neoj4.movieservice.mapper.ActorMapper;
import org.neoj4.movieservice.mapper.MovieMapper;
import org.neoj4.movieservice.model.ActedIn;
import org.neoj4.movieservice.model.Actor;
import org.neoj4.movieservice.model.Movie;
import org.neoj4.movieservice.model.dto.*;
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
    private final MovieMapper movieMapper;
    private final ActorMapper actorMapper;

    /*
    *
    *
    * TODO : try to integrate Dto's to our program  
     */

    public CompletableFuture<ApiResponse<MovieDto>> createMovie(CreateMovie movie ){
        return CompletableFuture.supplyAsync(() -> {
            Movie entityMovie = movieMapper.toEntity(movie);
            Movie repoSaved = repository.save(entityMovie);

            log.info("Created Movie {} {}" , repoSaved.getId() , repoSaved.getTitle());

            return ApiResponse.success(movieMapper.toDto(repoSaved));
        } , movieExecutor);
    }

    public CompletableFuture<ApiResponse<ActorDto>> createActor(CreateActor actor){
        return CompletableFuture.supplyAsync(() -> {
            Actor map = actorMapper.toEntity(actor);
            Actor saved = actorRepository.save(map);
            ActorDto actorDto = actorMapper.toDto(saved);

            log.info("Actor created {} {}" , saved.getId() , saved.getFullName());

            return ApiResponse.success(actorMapper.toDto(saved));

        }, movieExecutor);
    }


    public CompletableFuture<Void> linkActorToMovie(Long actorId , Long movieId,
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

        });
    }



    public CompletableFuture<ApiResponse<MovieDto>> getMovieById(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Movie movie = repository.findById(id).orElseThrow(() -> new RuntimeException("Movie not founded"));
            MovieDto movieDto = movieMapper.toDto(movie);
            log.info("Movie {}" , movie.getId());
            return ApiResponse.success(movieDto);
        } ,  movieExecutor);
    }

    public CompletableFuture<ApiResponse<ActorDto>> getActorById(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Actor actor = actorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Actor not found"));

            ActorDto dto = actorMapper.toDto(actor);
            log.info("founded Actor {} " , actor.getId());

            return ApiResponse.success(dto);
        } , movieExecutor);
    }



    public ApiResponse<List<MovieDto>> getAllMovies() {
        List<MovieDto> founded = repository.findAll()
                .stream()
                .map(movieMapper::toDto)
                .toList();
        return ApiResponse.success(founded);
    }

    public ApiResponse<List<ActorDto>> getAllActors() {
        List<ActorDto> founded = actorRepository.findAll()
                .stream()
                .map(actorMapper::toDto)
                .toList();

        return ApiResponse.success(founded);

    }


}