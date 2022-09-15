package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.announcement;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementMapper;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.dto.AnnouncementDetailsDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementMapper announcementMapper = Mappers.getMapper(AnnouncementMapper.class);

    @Override
    public Collection<AnnouncementDTO> getAllAnnouncements() {
        var announcements = this.announcementRepository.findAll();
        if (announcements.isEmpty())
            return Collections.emptyList();

        Collection<AnnouncementDTO> announcementDTOS = new ArrayList<>();
        announcements.forEach(a -> announcementDTOS.add(this.createAnnouncementDTO(a)));

        return announcementDTOS;
    }

    private AnnouncementDTO createAnnouncementDTO(AnnouncementEntity announcement) {
        var announcementDetails = announcement.getAnnouncementDetailsEntity();
        var announcementDTO = announcementMapper.announcementEntityToAnnouncementDTO(announcement);
        var announcementDetailsDTO = announcementMapper.announcementDetailsEntityToAnnouncementDetailsDTO(announcementDetails);
        announcementDTO.setAnnouncementDetailsDTO(announcementDetailsDTO);

        return announcementDTO;
    }
}
