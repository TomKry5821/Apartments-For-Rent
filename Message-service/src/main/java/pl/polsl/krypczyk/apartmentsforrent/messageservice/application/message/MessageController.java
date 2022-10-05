package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageService;

import javax.validation.Valid;

@RestController
@RequestMapping("/message/api/v1")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping(value = "/messages", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AddNewMessageResponse addNewMessage(@Valid @ModelAttribute AddNewMessageRequest addNewMessageRequest) {
        return this.messageService.addNewMessage(addNewMessageRequest);
    }
}
