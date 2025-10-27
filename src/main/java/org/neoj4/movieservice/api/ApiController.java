package org.neoj4.movieservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neoj4.movieservice.model.Actor;
import org.neoj4.movieservice.model.Movie;
import org.neoj4.movieservice.service.MovieActorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Schema(description = "that's our api for working with our db")
public class ApiController {

    private final MovieActorService service;


    @Schema(description = "create our movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "created") ,
            @ApiResponse(responseCode = "400" , description = "valid error")
    })
    @PostMapping( "/movie")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<Movie> save(@RequestBody Movie movie) {
        return service.createMovie(movie)
                .thenApply(mov -> {
                    log.info("Saved movie: {}", mov);
                    return mov;
                })
                .exceptionallyAsync(ex ->{
                    log.error("Error saving movie", ex);
                    throw new RuntimeException("Error saving movie", ex);
                });
    }

    @PostMapping("/actor")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<Actor> save(@RequestBody Actor actor) {
        return service.createActor(actor);
    }


    // http://localhost:8971/api/v1/4/3?role=Mark Zukerberg&year=2010
    @GetMapping("/{actorId}/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Void> link(@PathVariable Long actorId ,
                                        @PathVariable Long movieId,
                                        @RequestParam String role ,
                                        @RequestParam int year ) {
        return service.linkActorToMovie(actorId , movieId , role , year) ;
    }

    @GetMapping("/actor/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Actor> getActorById(@PathVariable Long id) {
        return service.getActorById(id);
    }


    @GetMapping("/movie/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Movie> getMovieById(@PathVariable Long id) {
        return service.getMovieById(id);
    }

    @GetMapping("/movie")
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getAllMovies() {
        return service.getAllMovies();
    }

    @GetMapping("/actor")
    @ResponseStatus(HttpStatus.OK)
    public List<Actor> getAllActor(){
        return service.getAllActors();
    }
}
