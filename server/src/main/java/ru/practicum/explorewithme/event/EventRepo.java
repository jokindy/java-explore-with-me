package ru.practicum.explorewithme.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(long userId, Pageable pageable);

    List<Event> findAllByState(EventState state);

    List<Event> findAllByStateAndPaid(EventState state, boolean paid);

    Event findEventByInitiatorId(long userId);

    @Modifying
    @Query("update Event e set e.state = ?1 where e.id = ?2")
    void cancelEvent(EventState state, long eventId);
}
