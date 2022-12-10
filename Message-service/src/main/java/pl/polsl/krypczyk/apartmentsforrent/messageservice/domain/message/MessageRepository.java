package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    Collection<MessageEntity> getMessageEntitiesBySenderIdAndReceiverId(Long senderId, Long ReceiverId);

    @Query("select distinct m.receiverId from MessageEntity m where m.senderId = ?1")
    Collection<Long> getAllReceiversBySenderId(Long senderId);
}
