package team16.literaryassociation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class Advices {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<?> handleNotFoundEntityException(NotFoundException e){
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(BadRequestException e){
        return error(HttpStatus.BAD_REQUEST,e);
    }


    private ResponseEntity<?> error(HttpStatus status, Exception e){
        return ResponseEntity.status(status).body(e.getMessage());
    }
}
