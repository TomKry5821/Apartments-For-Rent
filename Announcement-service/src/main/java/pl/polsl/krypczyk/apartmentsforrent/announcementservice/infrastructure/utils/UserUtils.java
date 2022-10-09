package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.utils;

import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;

@Component
public class UserUtils {

    public void checkIsUserIdValidElseThrowInvalidUser(Long userId, Long requesterId) throws InvalidUserIdException {
        if (!userId.equals(requesterId))
            throw new InvalidUserIdException();
    }

}
