package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class MessageControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(MethodArgumentNotValidException e) {
        var errorMessage = new StringBuilder();
        for (var error : e.getFieldErrors()) {
            errorMessage.append("Invalid ").append(error.getField()).append("\n");
        }

        e.printStackTrace();
        log.error(errorMessage.toString());
        return errorMessage.toString();
    }
}
