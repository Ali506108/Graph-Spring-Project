package org.neoj4.movieservice.api;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neoj4.movieservice.model.Actor;
import org.neoj4.movieservice.model.Movie;
import org.neoj4.movieservice.model.dto.ActorDto;
import org.neoj4.movieservice.model.dto.CreateActor;
import org.neoj4.movieservice.model.dto.CreateMovie;
import org.neoj4.movieservice.model.dto.MovieDto;
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
    public CompletableFuture<org.neoj4.movieservice.model.dto.ApiResponse<MovieDto>> save(@RequestBody CreateMovie movie) {
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

    @Schema(description = "create our actor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "created") ,
            @ApiResponse(responseCode = "400" , description = "valid error")
    })
    @PostMapping("/actor")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<org.neoj4.movieservice.model.dto.ApiResponse<ActorDto>> save(@RequestBody CreateActor actor) {
        return service.createActor(actor);
    }


    // http://localhost:8971/api/v1/4/3?role=Mark Zukerberg&year=2010
    @Schema(description = "linked out movie to our actor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202" , description = "OK"),
            @ApiResponse(responseCode = "400" , description = "Valid error")
    })
    @GetMapping("/{actorId}/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Void> link(@PathVariable Long actorId ,
                                        @PathVariable Long movieId,
                                        @RequestParam String role ,
                                        @RequestParam int year ) {
        return service.linkActorToMovie(actorId , movieId , role , year) ;
    }


    @Schema(description = "Get our actor with ID")
    @GetMapping("/actor/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<org.neoj4.movieservice.model.dto.ApiResponse<ActorDto>> getActorById(@PathVariable Long id) {
        return service.getActorById(id);
    }


    @Schema(description = "get our data with movie id")
    @GetMapping("/movie/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<org.neoj4.movieservice.model.dto.ApiResponse<MovieDto>> getMovieById(@PathVariable Long id) {
        return service.getMovieById(id);
    }

    @Schema(description = "get our All Data about Movie")
    @GetMapping("/movie")
    @ResponseStatus(HttpStatus.OK)
    public org.neoj4.movieservice.model.dto.ApiResponse<List<MovieDto>> getAllMovies() {
        return service.getAllMovies();
    }

    @Schema(description = "get our All data about Actors")
    @GetMapping("/actor")
    @ResponseStatus(HttpStatus.OK)
    public org.neoj4.movieservice.model.dto.ApiResponse<List<ActorDto>> getAllActor(){
        return service.getAllActors();
    }
}
