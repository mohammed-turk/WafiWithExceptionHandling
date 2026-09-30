package com.example.wafi.Advice;

import com.example.wafi.API.APIException;
import com.example.wafi.API.APIResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@org.springframework.web.bind.annotation.ControllerAdvice

public class ControllerAdvice {
    //    for in service file exceptions
    @ExceptionHandler(value = APIException.class)
    public ResponseEntity<?> APIException(APIException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    missing non nullable attribute
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<?> DataIntegrityViolationException(DataIntegrityViolationException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    wrong path naming
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<?> NoResourceFoundException(NoResourceFoundException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    huge integer number
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<?> HttpMessageNotReadableException(HttpMessageNotReadableException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    changed the raw to html in postman body
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> HttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    wrong type in url or param (ex: /users/abc when id is int)
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        String message = "Invalid value for '" + e.getName() + "'";
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    wrong http method (ex: GET on a POST endpoint)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        String message = e.getMessage();
        return ResponseEntity.status(405).body(new APIResponse(message));
    }

    //    wrong Accept header in the request
    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<?> HttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e){
        String message = e.getMessage();
        return ResponseEntity.status(406).body(new APIResponse(message));
    }

    //    bad sql (wrong table or column name)
    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<?> InvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException e){
        return ResponseEntity.status(500).body(new APIResponse("Database error"));
    }

    //    any other database error (spring wraps SQLException in this)
    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<?> DataAccessException(DataAccessException e){
        return ResponseEntity.status(500).body(new APIResponse("Database error"));
    }

    //    raw sql exception (if not wrapped by spring)
    @ExceptionHandler(value = SQLException.class)
    public ResponseEntity<?> SQLException(SQLException e){
        return ResponseEntity.status(500).body(new APIResponse("Database error"));
    }

    //    path variable in the method but not in the url mapping
    @ExceptionHandler(value = MissingPathVariableException.class)
    public ResponseEntity<?> MissingPathVariableException(MissingPathVariableException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    missing @RequestParam in the url
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<?> MissingServletRequestParameterException(MissingServletRequestParameterException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    validation failed on @Valid @RequestBody (ex: @NotNull, @Size)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidException(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    validation failed on @Validated params (path variable or request param)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> ConstraintViolationException(ConstraintViolationException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    validation annotation on the wrong type (ex: @Size on an int)
    @ExceptionHandler(value = UnexpectedTypeException.class)
    public ResponseEntity<?> UnexpectedTypeException(UnexpectedTypeException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }


    //    duplicate value or broken constraint that reaches the advice unwrapped by spring (ex: same email twice)
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }

    //    catch all for any exception not handled above
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> RuntimeException(RuntimeException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new APIResponse(message));
    }






}
