package com.bank.credsystem.exceptions.error;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomErrorDTO {

    private final int status;
    private final String error;
    private final String message;
    private List<String> detailedMessages;

    public CustomErrorDTO(HttpStatus httpStatus, String message, MethodArgumentNotValidException ex) {
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.setDetailedMessages(ex.getBindingResult().getAllErrors().stream()
                .map(err -> err.unwrap(ConstraintViolation.class))
                .map(err -> String.format("'%s' %s", err.getPropertyPath(), err.getMessage()))
                .collect(Collectors.toList()));
    }

}
