package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageService;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.security.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.security.exception.UnauthorizedUserException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("/message/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthorizationService authorizationService;
    private final MessageService messageService;

    @GetMapping(value = "/messages/{senderId}/conversation/{receiverId}")
    public Collection<MessageDTO> getConversation(@PathVariable @NotNull @Min(value = 1) Long senderId,
                                                  @PathVariable @NotNull @Min(value = 1) Long receiverId) throws UnauthorizedUserException {
        this.authorizationService.authorizeAdmin();
        return this.messageService.getConversation(senderId, receiverId);
    }


}
