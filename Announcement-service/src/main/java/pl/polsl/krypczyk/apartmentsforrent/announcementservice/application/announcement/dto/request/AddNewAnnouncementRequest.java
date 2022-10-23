package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Builder
@Data
public class AddNewAnnouncementRequest {

    @NotNull(message = "Invalid user id")
    @Min(value = 1, message = "User id cannot be lower than 1")
    private Long userId;

    @NotEmpty(message = "Invalid announcement title")
    private String title;

    @NotEmpty(message = "Invalid main photo path")
    private String mainPhotoPath;

    @NotNull(message = "Invalid rooms number")
    @Min(message = "rooms number cannot be lower than 1", value = 0)
    private Integer roomsNumber;

    @FutureOrPresent(message = "Invalid rental term")
    private LocalDate rentalTerm;

    @Min(value = 0, message = "Caution cannot be lower than 0")
    private BigDecimal caution;

    @Min(value = 0, message = "Rental amount cannot be lower than 0")
    private BigDecimal rentalAmount;

    @NotEmpty(message = "Invalid announcement content")
    private String content;

    @NotNull(message = "Photo paths cannot be null")
    private Collection<String> photoPaths;

    @NotEmpty(message = "Invalid district")
    private String district;

    @NotEmpty(message = "Invalid city")
    private String city;

    @Pattern(regexp = "^\\d{2}(?:[-\\s]\\d{3})?$", message = "Invalid zip code")
    private String zipCode;

    @NotEmpty(message = "Invalid street")
    private String street;

    @NotEmpty(message = "Invalid building number")
    private String buildingNumber;

    @Min(value = 0, message = "Local number cannot be lower than 1")
    @NotNull(message = "Local number cannot be null")
    private Integer localNumber;

}
