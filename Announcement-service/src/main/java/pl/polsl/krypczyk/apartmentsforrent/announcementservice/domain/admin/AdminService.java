package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.admin;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;

public interface AdminService {

    void deleteAnnouncement(Long announcementId) throws AnnouncementNotFoundException;
}
