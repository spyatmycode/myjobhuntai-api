package com.spyatmycode.myjobhuntai.advice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spyatmycode.myjobhuntai.dto.apiResponse.ApiResponse;
import com.spyatmycode.myjobhuntai.exception.FileTransferException;
import com.spyatmycode.myjobhuntai.exception.ParseDocumentException;
import com.spyatmycode.myjobhuntai.exception.ResourceNotFoundException;
import com.spyatmycode.myjobhuntai.exception.UserAlreadyExistsException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    //Handle exceptions thrown from the new job application dto validation failure
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> invalidJobApplicationArgumentHandler(MethodArgumentNotValidException exception){

        Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", exception.getMessage());
        Map<String, String> messages = new HashMap<>();
        for(FieldError fieldError: exception.getBindingResult().getFieldErrors()){
            messages.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        result.put("messages", messages);

        return ApiResponse.error(HttpStatus.BAD_REQUEST, result, "Validation error occured.", null);
    };

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> resourceNotFoundErroHandler(ResourceNotFoundException resourceNotFoundException){

         Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", resourceNotFoundException.getMessage());

        return ApiResponse.error(HttpStatus.NOT_FOUND, result, resourceNotFoundException.getMessage(), null);
    };

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
     public ResponseEntity<ApiResponse<Map<String, Object>>> sqlConstraintExceptionHandler(SQLIntegrityConstraintViolationException sqlException){

         Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", sqlException.getMessage());

        return ApiResponse.error(HttpStatus.BAD_REQUEST, result, sqlException.getMessage(), null);
    };

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> wrongArgumenttExceptionHandler(MethodArgumentTypeMismatchException wrongArgumentException){

         Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", wrongArgumentException.getMessage());

        return ApiResponse.error(HttpStatus.BAD_REQUEST, result, wrongArgumentException.getMessage(), null);
    };

    @ExceptionHandler(FileTransferException.class)
     public ResponseEntity<ApiResponse<Map<String, Object>>> fileTransferExceptionHandler(FileTransferException fileTransferException){

         Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", fileTransferException.getMessage());

        return ApiResponse.error(HttpStatus.BAD_REQUEST,result, fileTransferException.getMessage(), null);
    };

    @ExceptionHandler(ParseDocumentException.class)
     public ResponseEntity<ApiResponse<Map<String, Object>>> parseDocumentExceptionHandler(ParseDocumentException parseDocumentException){

         Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", parseDocumentException.getMessage());

        return ApiResponse.error(HttpStatus.BAD_REQUEST,result, parseDocumentException.getMessage(), null);
    };

    @ExceptionHandler(UserAlreadyExistsException.class)
     public ResponseEntity<ApiResponse<Map<String, Object>>> userAlreadyExistsExceptionHandler(UserAlreadyExistsException userAlreadyExistsException){

         Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", userAlreadyExistsException.getMessage());

        return ApiResponse.error(HttpStatus.BAD_REQUEST,result, userAlreadyExistsException.getMessage(), null);
    };

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> badCredentialsExceptionHandler(BadCredentialsException badCredentialsException){

         Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", badCredentialsException.getMessage());

        return ApiResponse.error(HttpStatus.BAD_REQUEST,result, badCredentialsException.getMessage(), null);
    };

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> usernameNotFoundExceptionHandler(UsernameNotFoundException usernameNotFoundException){

         Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", usernameNotFoundException.getMessage());

        return ApiResponse.error(HttpStatus.BAD_REQUEST,result, usernameNotFoundException.getMessage(), null);
    };

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> IllegalArgumentExceptionHandler(IllegalArgumentException illegalArgumentException){

         Map<String, Object> result = new HashMap<>();
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", illegalArgumentException.getMessage());

        String customMessage =  illegalArgumentException.getMessage() ;

        if(illegalArgumentException.getMessage().toLowerCase().contains("Id must not be null".toLowerCase())){
            customMessage = illegalArgumentException.getMessage() + ". Candidate profile most likely not created.";
        }

        return ApiResponse.error(HttpStatus.BAD_REQUEST,result, customMessage, null);
    };

    




    
}
