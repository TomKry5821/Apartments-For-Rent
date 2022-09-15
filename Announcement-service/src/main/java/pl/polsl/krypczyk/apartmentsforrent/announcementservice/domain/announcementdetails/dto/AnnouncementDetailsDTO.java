package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AnnouncementDetailsDTO {

    private String title;

    private String mainPhotoPath;

    private Integer roomsNumber;

    private LocalDate rentalTerm;

    private BigDecimal caution;

    private BigDecimal rentalAmount;

}
