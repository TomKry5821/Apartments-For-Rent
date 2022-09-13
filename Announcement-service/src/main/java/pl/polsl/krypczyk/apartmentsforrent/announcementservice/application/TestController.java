package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/announcement/api/v1/test")
public class TestController {

    @GetMapping()
    public String test() {
        return "Announcement controller check";
    }
}
