package org.neoj4.movieservice.model.dto;

import org.neoj4.movieservice.model.ActedIn;

import java.util.HashSet;

public record ActorDto(
        Long id ,
        String fullName ,
        int age ,
        HashSet<ActedIn> actedIns
) {
}
