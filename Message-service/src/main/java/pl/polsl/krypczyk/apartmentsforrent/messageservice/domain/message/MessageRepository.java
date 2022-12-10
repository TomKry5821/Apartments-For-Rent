package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    Collection<MessageEntity> getMessageEntitiesBySenderIdAndReceiverId(Long senderId, Long ReceiverId);

    @Query("select distinct m.receiverId from MessageEntity m where m.senderId = ?1")
    Set<Long> getAllReceiversBySenderId(Long senderId);

    @Query("select distinct m.senderId from MessageEntity m where m.receiverId = ?1")
    Set<Long> getAllSendersByReceiverId(Long receiverId);
}
