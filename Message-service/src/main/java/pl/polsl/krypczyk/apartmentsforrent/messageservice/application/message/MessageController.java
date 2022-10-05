package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageService;

@RestController
@RequestMapping("message/api/v1/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
}
