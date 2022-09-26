package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
    public GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(@NotNull @PathVariable("announcementId") Long announcementId) throws AnnouncementNotFoundException {
        return this.announcementService.getAnnouncementWithAllDetails(announcementId);
    }

    @PostMapping("/announcements")
    public AddNewAnnouncementResponse addNewAnnouncement(@RequestBody @Valid AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                         @RequestHeader("requester-user-id") @NotNull Long requesterId) throws InvalidUserIdException {
        return this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, requesterId);
    }

    @PutMapping("/announcements/{announcementId}")
    public UpdateAnnouncementResponse updateAnnouncement(@RequestBody @Valid UpdateAnnouncementRequest updateAnnouncementRequest,
                                                         @PathVariable("announcementId") @NotNull Long announcementId,
                                                         @RequestHeader("requester-user-id") @NotNull Long requesterId) throws AnnouncementNotFoundException, ClosedAnnouncementException, InvalidUserIdException {
        return this.announcementService.updateAnnouncement(updateAnnouncementRequest, announcementId, requesterId);
    }

    @PostMapping("/announcements/{announcementId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeAnnouncement(@PathVariable("announcementId") @NotNull Long announcementId,
                                  @RequestHeader("requester-user-id") @NotNull Long requesterId) throws AnnouncementNotFoundException, InvalidUserIdException {
        this.announcementService.closeAnnouncement(announcementId, requesterId);
    }

    @PostMapping("/announcements/{announcementId}/observe/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ObserveAnnouncementResponse observeAnnouncement(@NotNull @Min(value = 1) @PathVariable("announcementId") Long announcementId,
                                                           @NotNull @Min(value = 1) @PathVariable("userId") Long userId,
                                                           @RequestHeader("requester-user-id") @NotNull Long requesterId) throws InvalidUserIdException, AnnouncementNotFoundException, AnnouncementAlreadyObservedException {
        return this.announcementService.observeAnnouncement(announcementId, userId, requesterId);
    }

    @DeleteMapping("/announcements/{announcementId}/unobserve/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unobserveAnnouncement(@NotNull @Min(value = 1) @PathVariable("announcementId") Long announcementId,
                                      @NotNull @Min(value = 1) @PathVariable("userId") Long userId,
                                      @RequestHeader("requester-user-id") @NotNull Long requesterId) throws InvalidUserIdException, AnnouncementNotFoundException {
        this.announcementService.unobserveAnnouncement(announcementId, userId, requesterId);
    }

}
