package ru.practicum.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Request findByRequesterIdAndEventId(long userId, long eventId);

    List<Request> findAllByRequesterId(long userId);

    @Query(value = "select count(*) from requests where event_id = ?1 and status = 'CONFIRMED'", nativeQuery = true)
    Integer getConfirmedRequests(long eventId);
}
