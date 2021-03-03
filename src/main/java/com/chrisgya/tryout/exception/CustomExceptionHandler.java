package com.chrisgya.tryout.exception;


import com.chrisgya.tryout.model.ErrorMessage;
import com.chrisgya.tryout.model.ValidationError;
import com.chrisgya.tryout.util.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.chrisgya.tryout.util.SQLUtils.conflictSQLErrorMessage;

@Slf4j
@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<?> handleBadRequestExceptions(BadRequestException e, WebRequest webRequest) {
        log.error("BadRequestException:: {}", e);
        var response = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<?> handleNotFoundExceptions(NotFoundException e, WebRequest webRequest) {
        var response = new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<?> ConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        List<ValidationError> validationErrors = new ArrayList<>();
        constraintViolations.stream()
                .forEach(constraintViolation -> {
                    String fieldName = null;
                    for (Path.Node node : constraintViolation.getPropertyPath()) {
                        fieldName = node.getName();
                    }
                    validationErrors.add(new ValidationError(fieldName, constraintViolation.getMessage()));
                });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ValidationError> validationErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            var errorMessage = objectError.getDefaultMessage();
            var fieldName = ((FieldError) objectError).getField();
            validationErrors.add(new ValidationError(fieldName, errorMessage));
        });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public final ResponseEntity<?> handleDataAccessException(DataAccessException e, WebRequest webRequest) {
        log.error("DataAccessException:: {}", e);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = SQLUtils.extractSQLErrorMessage(e.getMessage());

        if (e.getMessage().contains("duplicate key")) {
            message = conflictSQLErrorMessage(e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        } else if (e.getMessage().contains("The INSERT statement conflicted with the FOREIGN KEY constraint")) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        var response = new ErrorMessage(httpStatus.value(), new Date(), message, webRequest.getDescription(false));
        return new ResponseEntity<>(response, httpStatus);
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> UnhandledExceptions(Exception e) {
        log.error("Exception:: {}", e);

        var response = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), e.getMessage(), "");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
