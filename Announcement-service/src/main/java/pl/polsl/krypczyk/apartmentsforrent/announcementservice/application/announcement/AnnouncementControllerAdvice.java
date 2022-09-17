package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;

@ControllerAdvice
public class AnnouncementControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(MethodArgumentNotValidException e) {
        var errorMessage = new StringBuilder();
        for (var error : e.getFieldErrors()) {
            errorMessage.append("Invalid ").append(error.getField()).append("\n");
        }

        return errorMessage.toString();
    }

    @ExceptionHandler(AnnouncementNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(AnnouncementNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ClosedAnnouncementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(ClosedAnnouncementException e) {
        return e.getMessage();
    }
}
