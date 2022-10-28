package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Data
@Builder
@ToString
public class GetAnnouncementWithAllDetailsResponse {
    private Long userId;

    private String username;

    private String title;

    private String mainPhotoPath;

    private Integer roomsNumber;

    private LocalDate rentalTerm;

    private BigDecimal caution;

    private BigDecimal rentalAmount;

    private String content;

    private Collection<String> photoPaths;

    private String district;

    private String city;

    private String zipCode;

    private String street;

    private String buildingNumber;

    private Integer localNumber;

    private LocalDate creationDate;

    private Boolean isClosed;

}
