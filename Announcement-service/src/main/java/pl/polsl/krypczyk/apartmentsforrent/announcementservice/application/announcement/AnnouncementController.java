package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto.ObservedAnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.security.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.security.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementService;
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

    private final ObservedAnnouncementService observedAnnouncementService;

    private final AuthorizationService authorizationService;

    @GetMapping("/public/announcements")
    public Collection<AnnouncementDTO> getAllActiveAnnouncements() {
        return this.announcementService.getAllActiveAnnouncements();
    }

    @GetMapping("/public/announcements/{announcementId}")
    public GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(@NotNull @PathVariable("announcementId") Long announcementId) throws AnnouncementNotFoundException {
        return this.announcementService.getAnnouncementWithAllDetails(announcementId);
    }

    @PostMapping("/announcements")
    public AddNewAnnouncementResponse addNewAnnouncement(@RequestBody @Valid AddNewAnnouncementRequest addNewAnnouncementRequest) throws UnauthorizedUserException {
        this.authorizationService.authorizeUser(addNewAnnouncementRequest.getUserId());
        return this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
    }

    @PutMapping("/announcements/{announcementId}")
    public UpdateAnnouncementResponse updateAnnouncement(@RequestBody @Valid UpdateAnnouncementRequest updateAnnouncementRequest,
                                                         @PathVariable("announcementId") @NotNull Long announcementId) throws AnnouncementNotFoundException, ClosedAnnouncementException, UnauthorizedUserException {
        this.authorizationService.authorizeUser(updateAnnouncementRequest.getUserId());
        return this.announcementService.updateAnnouncement(updateAnnouncementRequest, announcementId);
    }

    @PostMapping("/announcements/{announcementId}/close/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeAnnouncement(@PathVariable("announcementId") @NotNull Long announcementId,
                                  @PathVariable("userId") @NotNull Long userId) throws AnnouncementNotFoundException, UnauthorizedUserException {
        this.authorizationService.authorizeUser(userId);
        this.announcementService.closeAnnouncement(announcementId, userId);
    }

    @PostMapping("/announcements/{announcementId}/observe/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ObserveAnnouncementResponse observeAnnouncement(@NotNull @Min(value = 1) @PathVariable("announcementId") Long announcementId,
                                                           @NotNull @Min(value = 1) @PathVariable("userId") Long userId) throws AnnouncementNotFoundException, AnnouncementAlreadyObservedException, ClosedAnnouncementException, UnauthorizedUserException {
        this.authorizationService.authorizeUser(userId);
        return this.observedAnnouncementService.observeAnnouncement(announcementId, userId);
    }

    @DeleteMapping("/announcements/{announcementId}/unobserve/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unobserveAnnouncement(@NotNull @Min(value = 1) @PathVariable("announcementId") Long announcementId,
                                      @NotNull @Min(value = 1) @PathVariable("userId") Long userId) throws AnnouncementNotFoundException, UnauthorizedUserException {
        this.authorizationService.authorizeUser(userId);
        this.observedAnnouncementService.unobserveAnnouncement(announcementId, userId);
    }

    @GetMapping("/announcements/observed/{userId}")
    public Collection<ObservedAnnouncementDTO> getObservedAnnouncements(@NotNull @Min(value = 1) @PathVariable("userId") Long userId) throws UnauthorizedUserException {
        this.authorizationService.authorizeUser(userId);
        return this.observedAnnouncementService.getObservedAnnouncements(userId);
    }

}
