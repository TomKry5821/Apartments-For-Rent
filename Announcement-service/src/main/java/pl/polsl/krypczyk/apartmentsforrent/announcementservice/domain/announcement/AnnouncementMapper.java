package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcementdetails.dto.AnnouncementDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;


@Mapper
public interface AnnouncementMapper {
    AnnouncementDetailsDTO announcementDetailsEntityToAnnouncementDetailsDTO(AnnouncementDetailsEntity announcementDetailsEntity);
    @Mapping(target = "announcementDetailsDTO", ignore = true)
    AnnouncementDTO announcementEntityToAnnouncementDTO(AnnouncementEntity announcementEntity);
    @Mapping(target = "announcementId", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "isClosed", ignore = true)
    AddNewAnnouncementResponse createAnnouncementRequestToCreateAnnouncementResponse(AddNewAnnouncementRequest addNewAnnouncementRequest);
    UpdateAnnouncementResponse updateAnnouncementRequestToUpdateAnnouncementResponse(UpdateAnnouncementRequest updateAnnouncementRequest);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "isClosed", ignore = true)
    @Mapping(target = "announcementDetailsEntity", ignore = true)
    AnnouncementEntity addAnnouncementRequestDtoToAnnouncementEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressDetailsEntity", ignore = true)
    @Mapping(target = "announcementContent", ignore = true)
    AnnouncementDetailsEntity addAnnouncementRequestDtoToAnnouncementDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    @Mapping(target = "id", ignore = true)
    AnnouncementContentEntity addAnnouncementRequestDtoToAnnouncementContentEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    @Named("stringToPhotoPathEntity")
    @Mapping(target = "id", ignore = true)
    PhotoPathEntity photoPathToPhotoPathEntity(String photoPath);
    @Mapping(target = "id", ignore = true)
    AddressDetailsEntity addAnnouncementRequestDtoToAddressDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    default PhotoPathEntity stringToPhotoPathEntity(String photoPath){
        var photoPathEntity = new PhotoPathEntity();
        photoPathEntity.setPhotoPath(photoPath);
        return photoPathEntity;
    }
}
