package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

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

    @Column(name = "STREET", nullable = false)
    private String street;

    @Column(name = "ZIP_CODE", nullable = false)
    private String zipCode;

    @Column(name = "BUILDING_NUMBER", nullable = false)
    private String buildingNumber;

    @Column(name = "LOCAL_NUMBER", nullable = false)
    private Integer localNumber;

}
