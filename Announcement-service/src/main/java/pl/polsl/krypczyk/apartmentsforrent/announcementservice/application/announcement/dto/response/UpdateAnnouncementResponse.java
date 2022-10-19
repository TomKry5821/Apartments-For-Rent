package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Data
@Builder
public class UpdateAnnouncementResponse {

    private Long userId;

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
}
