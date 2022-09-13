package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "ADDRESS_DETAILS")
public class AddressDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Column(name = "STREET", nullable = false)
    private String street;

    @Column(name = "DISTRICT", nullable = false)
    private String district;

    @Column(name = "ZIP_CODE", nullable = false)
    private String zipCode;

    @Column(name = "BUILDING_NUMBER", nullable = false)
    private String buildingNumber;

    @Column(name = "LOCAL_NUMBER", nullable = false)
    private Integer localNumber;

}
