package SogangSolutionShare.BE.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e){
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }
    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<ErrorResponse> handlerUnauthorizedException(ForbiddenException e){
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.FORBIDDEN;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }
    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlerMemberNotFoundException(MemberNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.NOT_FOUND;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlerCategoryNotFoundException(CategoryNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.NOT_FOUND;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }
    @ExceptionHandler(QuestionNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlerQuestionNotFoundException(QuestionNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.NOT_FOUND;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(TagNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlerTagNotFoundException(TagNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.NOT_FOUND;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }
}
