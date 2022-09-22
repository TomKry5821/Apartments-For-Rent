package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcementdetails.dto.AnnouncementDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;

import java.util.ArrayList;
import java.util.Collection;

@Mapper
public interface AnnouncementMapper {
    AnnouncementDetailsDTO announcementDetailsEntityToAnnouncementDetailsDTO(AnnouncementDetailsEntity announcementDetailsEntity);
    AnnouncementDTO announcementEntityToAnnouncementDTO(AnnouncementEntity announcementEntity);
    AddNewAnnouncementResponse createAnnouncementRequestToCreateAnnouncementResponse(AddNewAnnouncementRequest addNewAnnouncementRequest);
    UpdateAnnouncementResponse updateAnnouncementRequestToUpdateAnnouncementResponse(UpdateAnnouncementRequest updateAnnouncementRequest);
    AnnouncementEntity addAnnouncementRequestDtoToAnnouncementEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    AnnouncementDetailsEntity addAnnouncementRequestDtoToAnnouncementDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    AnnouncementContentEntity addAnnouncementRequestDtoToAnnouncementContentEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    @Named("stringToPhotoPathEntity")
    PhotoPathEntity photoPathToPhotoPathEntity(String photoPath);
    AddressDetailsEntity addAnnouncementRequestDtoToAddressDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    default PhotoPathEntity stringToPhotoPathEntity(String photoPath){
        var photoPathEntity = new PhotoPathEntity();
        photoPathEntity.setPhotoPath(photoPath);
        return photoPathEntity;
    }
}
