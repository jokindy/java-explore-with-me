package ru.practicum.explorewithme.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.user.subscribers.SubscriberStatus;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id in ?1")
    List<User> findAllByIds(Long[] ids, Pageable pageable);

    @Modifying
    @Query(value = "update subscribers set status = ?3 where user_id = ?1 and subscriber_id = ?2", nativeQuery = true)
    void handleSubscriber(long userId, long subscriberId, SubscriberStatus status);
}
