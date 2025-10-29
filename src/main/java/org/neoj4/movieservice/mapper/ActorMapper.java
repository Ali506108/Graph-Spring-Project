package org.neoj4.movieservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.neoj4.movieservice.model.Actor;
import org.neoj4.movieservice.model.dto.ActorDto;
import org.neoj4.movieservice.model.dto.CreateActor;

@Mapper(componentModel = "spring" )
public interface ActorMapper {



    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "actedIns" , ignore = true)
    ActorDto toDto( Actor actor );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actedIns", ignore = true)
    Actor toEntity( CreateActor actorDto );
}