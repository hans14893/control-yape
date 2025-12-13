package com.control.yape.api;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * Payload de error consistente para las respuestas de la API.
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        List<String> details,
        String path
) {

    public static ErrorResponse of(HttpStatus status, String message, List<String> details, String path) {
        List<String> safeDetails = details == null ? List.of() : List.copyOf(details);
        return new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, safeDetails, path);
    }
}
