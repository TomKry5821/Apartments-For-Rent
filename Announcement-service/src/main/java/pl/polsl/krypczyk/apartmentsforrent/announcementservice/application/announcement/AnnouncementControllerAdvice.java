package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;

@ControllerAdvice
@Slf4j
public class AnnouncementControllerAdvice {

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

    @ExceptionHandler(AnnouncementNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(AnnouncementNotFoundException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(ClosedAnnouncementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(ClosedAnnouncementException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(InvalidUserIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(InvalidUserIdException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(AnnouncementAlreadyObservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(AnnouncementAlreadyObservedException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return e.getMessage();
    }
}
