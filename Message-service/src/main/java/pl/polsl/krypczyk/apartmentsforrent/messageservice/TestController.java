package pl.polsl.krypczyk.apartmentsforrent.messageservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message/api/v1/test")
public class TestController {

    @GetMapping()
    public String test() {
        return "Message controller check";
    }
}
