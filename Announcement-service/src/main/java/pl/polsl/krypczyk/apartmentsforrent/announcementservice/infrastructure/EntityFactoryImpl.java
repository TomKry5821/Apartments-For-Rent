package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementMapper;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EntityFactoryImpl implements EntityFactory {

    private final AnnouncementMapper announcementMapper = Mappers.getMapper(AnnouncementMapper.class);

    @Override
    public AnnouncementEntity createAnnouncementEntity(AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                       AnnouncementDetailsEntity announcementDetails) {
        var announcement = this.announcementMapper.addAnnouncementRequestDtoToAnnouncementEntity(addNewAnnouncementRequest);
        announcement.setAnnouncementDetailsEntity(announcementDetails);
        announcement.setCreationDate(LocalDate.now());
        announcement.setIsClosed(false);

        log.trace("Created announcement entity - " + announcement);
        return announcement;
    }

    @Override
    public AnnouncementDetailsEntity createAnnouncementDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                                     AnnouncementContentEntity announcementContent,
                                                                     AddressDetailsEntity addressDetails) {
        var announcementDetails = this.announcementMapper.addAnnouncementRequestDtoToAnnouncementDetailsEntity(addNewAnnouncementRequest);
        announcementDetails.setAnnouncementContent(announcementContent);
        announcementDetails.setAddressDetailsEntity(addressDetails);

        log.trace("Created announcement details entity - " + announcementDetails);
        return announcementDetails;
    }

    @Override
    public AnnouncementContentEntity createAnnouncementContentEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcementContent = this.announcementMapper.addAnnouncementRequestDtoToAnnouncementContentEntity(addNewAnnouncementRequest);

        var photoPaths = new ArrayList<PhotoPathEntity>();
        addNewAnnouncementRequest.getPhotoPaths().forEach((r) -> {
            var photoPath = this.announcementMapper.photoPathToPhotoPathEntity(r);
            photoPaths.add(photoPath);
        });
        announcementContent.setPhotoPaths(photoPaths);

        log.trace("Created announcement content entity - " + announcementContent);
        return announcementContent;
    }

    @Override
    public AddressDetailsEntity createAddressDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var addressDetails = this.announcementMapper.addAnnouncementRequestDtoToAddressDetailsEntity(addNewAnnouncementRequest);

        log.trace("Created address details entity - " + addressDetails);
        return addressDetails;
    }

    @Override
    public ObservedAnnouncementEntity createObservedAnnouncementEntity(AnnouncementEntity announcement, Long userId){
        var observedAnnouncement = new ObservedAnnouncementEntity();
        observedAnnouncement.setObservingUserId(userId);
        observedAnnouncement.setAnnouncementEntity(announcement);

        log.trace("Created observed announcement entity - " + observedAnnouncement);
        return observedAnnouncement;
    }
}
