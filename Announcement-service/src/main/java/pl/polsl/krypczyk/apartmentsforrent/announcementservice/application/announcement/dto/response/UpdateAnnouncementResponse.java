package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@ToString
public class UpdateAnnouncementResponse {

    private Long userId;

    private String title;

    private Integer roomsNumber;

    private LocalDate rentalTerm;

    private BigDecimal caution;

    private BigDecimal rentalAmount;

    private String content;
    
    private String district;

    private String city;

    private String zipCode;

    private String street;

    private String buildingNumber;

    private Integer localNumber;
}
