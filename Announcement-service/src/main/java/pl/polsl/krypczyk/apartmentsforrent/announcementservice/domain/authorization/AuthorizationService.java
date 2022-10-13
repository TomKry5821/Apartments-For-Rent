package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.authorization;

public interface AuthorizationService {

    void authorizeUser(Long userId);

    void authorizeAdmin(Long adminId);

}
