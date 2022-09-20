package ru.practicum.explorewithme.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompilationRepo extends JpaRepository<Compilation, Long> {

    @Modifying
    @Query(value = "delete from event_compilations where compilation_id = ?1 and event_id = ?2", nativeQuery = true)
    void deleteEventFromCompilation(long compId, long eventId);

    @Modifying
    @Query(value = "insert into event_compilations values ( ?1, ?2 )", nativeQuery = true)
    void addEventToCompilation(long compId, long eventId);

    @Modifying
    @Query(value = "update Compilation c set c.pinned = ?1 where c.id = ?2")
    void setCompilationPinned(boolean pinned, long compId);

    List<Compilation> findAllByPinned(boolean pinned);
}
