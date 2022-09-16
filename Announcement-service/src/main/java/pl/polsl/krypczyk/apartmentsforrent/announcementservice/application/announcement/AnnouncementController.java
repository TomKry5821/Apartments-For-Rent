package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.CreateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.CreateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.GetAnnouncementWithAllDetailsResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("/announcement/api/v1")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/public/announcements")
    public Collection<AnnouncementDTO> getAllAnnouncements() {
        return this.announcementService.getAllAnnouncements();
    }

    @GetMapping("/public/announcements/{announcementId}")
    public GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(@NotNull @PathVariable("announcementId") Long announcementId){
        return this.announcementService.getAnnouncementWithAllDetails(announcementId);
    }

    @PostMapping("/announcements")
    public CreateAnnouncementResponse createAnnouncement(@RequestBody @Valid CreateAnnouncementRequest createAnnouncementRequest){
        return this.announcementService.createAnnouncement(createAnnouncementRequest);
    }


}
