package org.neoj4.movieservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.neoj4.movieservice.model.Movie;
import org.neoj4.movieservice.model.dto.CreateMovie;
import org.neoj4.movieservice.model.dto.MovieDto;

@Mapper(componentModel = "spring" , uses = {})
public interface MovieMapper {


    @Mapping(target = "id" , ignore = true)
    Movie toEntity(CreateMovie movieDto );

    MovieDto toDto( Movie movie );
}
