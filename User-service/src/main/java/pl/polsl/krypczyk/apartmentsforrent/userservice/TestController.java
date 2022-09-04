package pl.polsl.krypczyk.apartmentsforrent.userservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api/v1/test")
public class TestController {

    @GetMapping()
    public String test() {
        return "User controller check";
    }

}
