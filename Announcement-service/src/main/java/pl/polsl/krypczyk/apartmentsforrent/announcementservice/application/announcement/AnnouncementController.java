package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;

import java.util.Collection;

@RestController
@RequestMapping("/announcement/api/v1")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/announcements")
    public Collection<AnnouncementDTO> getAllAnnouncements() {
        return this.announcementService.getAllAnnouncements();
    }

}
