package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/announcement/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/announcements/{announcementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnnouncement(@PathVariable("announcementId") @NotNull @Min(value = 1) Long announcementId) throws AnnouncementNotFoundException {
        this.adminService.deleteAnnouncement(announcementId);
    }
}
