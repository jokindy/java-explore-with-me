package ru.practicum.explorewithme.request;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "requests")
@NoArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "requester_id")
    private Long requesterId;
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public Request(long userId, long eventId, RequestStatus status) {
        this.requesterId = userId;
        this.eventId = eventId;
        this.created = LocalDateTime.now();
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(id, request.id) &&
                Objects.equals(eventId, request.eventId) &&
                Objects.equals(requesterId, request.requesterId) &&
                Objects.equals(created, request.created) && status == request.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId, requesterId, created, status);
    }
}
