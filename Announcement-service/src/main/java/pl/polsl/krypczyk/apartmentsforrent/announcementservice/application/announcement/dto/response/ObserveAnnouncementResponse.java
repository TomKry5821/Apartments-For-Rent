package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObserveAnnouncementResponse {

    private Long userId;

    private Long announcementId;

}
