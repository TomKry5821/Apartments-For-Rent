package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcementdetails.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@ToString
public class AnnouncementDetailsDTO {

    private String title;

    @ToString.Exclude
    private byte[] mainPhoto;

    private Integer roomsNumber;

    private LocalDate rentalTerm;

    private BigDecimal caution;

    private BigDecimal rentalAmount;

}
