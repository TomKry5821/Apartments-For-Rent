package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcementdetails.dto.AnnouncementDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photo.PhotoEntity;

import java.io.IOException;


@Mapper
public interface AnnouncementMapper {
    AnnouncementDetailsDTO announcementDetailsEntityToAnnouncementDetailsDTO(AnnouncementDetailsEntity announcementDetailsEntity);
    @Mapping(target = "announcementDetailsDTO", ignore = true)
    @Mapping(target = "username", ignore = true)
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
    @Named("multipartFileToByteArray")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressDetailsEntity", ignore = true)
    @Mapping(target = "announcementContent", ignore = true)
    AnnouncementDetailsEntity addAnnouncementRequestDtoToAnnouncementDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    @Mapping(target = "id", ignore = true)
    AnnouncementContentEntity addAnnouncementRequestDtoToAnnouncementContentEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    @Mapping(target = "id", ignore = true)
    AddressDetailsEntity addAnnouncementRequestDtoToAddressDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);
    default PhotoEntity multipartFileToPhotoEntity(MultipartFile photo) throws IOException {
        var photoPathEntity = new PhotoEntity();
        photoPathEntity.setPhoto(photo.getBytes());
        return photoPathEntity;
    }
    default byte[] multipartFileToByteArray(MultipartFile photo) throws IOException {
        return photo.getBytes();
    }
}
