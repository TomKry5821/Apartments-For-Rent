package pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

@ControllerAdvice
public class AuthorizationControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(UserNotFoundException e){
        return e.getMessage();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(BadCredentialsException e){
        return e.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(UserAlreadyExistsException e){
        return e.getMessage();
    }

    @ExceptionHandler(InactiveAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(InactiveAccountException e){
        return e.getMessage();
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleException(UnauthorizedUserException e){
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(MethodArgumentNotValidException e){
        return e.getFieldErrors().toString();
    }
}
