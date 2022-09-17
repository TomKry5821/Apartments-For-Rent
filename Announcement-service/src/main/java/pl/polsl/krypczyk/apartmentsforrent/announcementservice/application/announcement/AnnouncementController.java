package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.UpdateAnnouncementResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("/announcement/api/v1")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/public/announcements")
    public Collection<AnnouncementDTO> getAllActiveAnnouncements() {
        return this.announcementService.getAllActiveAnnouncements();
    }

    @GetMapping("/public/announcements/{announcementId}")
    public GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(@NotNull @PathVariable("announcementId") Long announcementId) {
        return this.announcementService.getAnnouncementWithAllDetails(announcementId);
    }

    @PostMapping("/announcements")
    public AddNewAnnouncementResponse addNewAnnouncement(@RequestBody @Valid AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                         @RequestHeader("requester-user-id") @NotNull Long requesterId) {
        return this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, requesterId);
    }

    @PutMapping("/announcements/{announcementId}")
    public UpdateAnnouncementResponse updateAnnouncement(@RequestBody @Valid UpdateAnnouncementRequest updateAnnouncementRequest,
                                                         @PathVariable("announcementId") @NotNull Long announcementId,
                                                         @RequestHeader("requester-user-id") @NotNull Long requesterId) {
        return this.announcementService.updateAnnouncement(updateAnnouncementRequest, announcementId, requesterId);
    }

    @PostMapping("/announcements/{announcementId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeAnnouncement(@PathVariable("announcementId") @NotNull Long announcementId,
                                  @RequestHeader("requester-user-id") @NotNull Long requesterId) {
        this.announcementService.closeAnnouncement(announcementId, requesterId);
    }


}
