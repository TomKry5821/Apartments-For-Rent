package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.ConversationDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageService;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.security.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.security.exception.UnauthorizedUserException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("/message/api/v1")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    private final AuthorizationService authorizationService;

    @PostMapping(value = "/messages", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AddNewMessageResponse addNewMessage(@Valid @ModelAttribute AddNewMessageRequest addNewMessageRequest) throws UnauthorizedUserException {
        this.authorizationService.authorizeUser(addNewMessageRequest.getSenderId());
        return this.messageService.addNewMessage(addNewMessageRequest);
    }

    @GetMapping(value = "/messages/{senderId}/conversation/{receiverId}")
    public Collection<MessageDTO> getConversation(@PathVariable @NotNull @Min(value = 1) Long senderId,
                                                  @PathVariable @NotNull @Min(value = 1) Long receiverId) throws UnauthorizedUserException {
        this.authorizationService.authorizeUser(senderId);
        return this.messageService.getConversation(senderId, receiverId);
    }

    @GetMapping(value = "/messages/conversations/{userId}")
    public Collection<ConversationDTO> getConversation(@PathVariable @NotNull @Min(value = 1) Long userId) throws UnauthorizedUserException {
        this.authorizationService.authorizeUser(userId);
        return this.messageService.getUserConversations(userId);
    }
}
