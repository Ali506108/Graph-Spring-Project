package org.neoj4.movieservice.model.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String error,
        String message ,
        String path,
        int status,
        LocalDateTime now
        ) {
}
