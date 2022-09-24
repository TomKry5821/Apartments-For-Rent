package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementMapper;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathRepository;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
@Transactional
@RequiredArgsConstructor
public class EntityFactoryImpl implements EntityFactory {

    private final PhotoPathRepository photoPathRepository;
    private final AnnouncementContentRepository announcementContentRepository;
    private final AddressDetailsRepository addressDetailsRepository;
    private final AnnouncementDetailsRepository announcementDetailsRepository;
    private final AnnouncementRepository announcementRepository;
    private final ObservedAnnouncementRepository observedAnnouncementRepository;
    private final AnnouncementMapper announcementMapper = Mappers.getMapper(AnnouncementMapper.class);

    @Override
    public AnnouncementEntity createAnnouncementEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcement = this.announcementMapper.addAnnouncementRequestDtoToAnnouncementEntity(addNewAnnouncementRequest);
        announcement.setAnnouncementDetailsEntity(this.createAnnouncementDetailsEntity(addNewAnnouncementRequest));
        announcement.setCreationDate(LocalDate.now());
        announcement.setIsClosed(false);

        return this.announcementRepository.save(announcement);
    }

    @Override
    public AnnouncementDetailsEntity createAnnouncementDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcementDetails = this.announcementMapper.addAnnouncementRequestDtoToAnnouncementDetailsEntity(addNewAnnouncementRequest);
        announcementDetails.setAnnouncementContent(this.createAnnouncementContentEntity(addNewAnnouncementRequest));
        announcementDetails.setAddressDetailsEntity(this.createAddressDetailsEntity(addNewAnnouncementRequest));

        return this.announcementDetailsRepository.save(announcementDetails);
    }

    @Override
    public AnnouncementContentEntity createAnnouncementContentEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcementContent = this.announcementMapper.addAnnouncementRequestDtoToAnnouncementContentEntity(addNewAnnouncementRequest);

        var photoPaths = new ArrayList<PhotoPathEntity>();
        addNewAnnouncementRequest.getPhotoPaths().forEach((r) -> {
            var photoPath = this.announcementMapper.photoPathToPhotoPathEntity(r);
            photoPaths.add(this.photoPathRepository.save(photoPath));
        });
        announcementContent.setPhotoPaths(photoPaths);

        return this.announcementContentRepository.save(announcementContent);
    }

    @Override
    public AddressDetailsEntity createAddressDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var addressDetails = this.announcementMapper.addAnnouncementRequestDtoToAddressDetailsEntity(addNewAnnouncementRequest);
        return this.addressDetailsRepository.save(addressDetails);
    }

    @Override
    public ObservedAnnouncementEntity createObservedAnnouncementEntity(AnnouncementEntity announcement, Long userId){
        var observedAnnouncement = new ObservedAnnouncementEntity();
        observedAnnouncement.setObservingUserId(userId);
        observedAnnouncement.setAnnouncementEntity(announcement);
        return this.observedAnnouncementRepository.save(observedAnnouncement);
    }
}
