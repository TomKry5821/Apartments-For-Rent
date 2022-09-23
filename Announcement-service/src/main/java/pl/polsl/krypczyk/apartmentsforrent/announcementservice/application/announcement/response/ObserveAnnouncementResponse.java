package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObserveAnnouncementResponse {

    Long userId;

    Long announcementId;

}
