package ru.practicum.explorewithme.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.request.RequestStatus;
import ru.practicum.explorewithme.user.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "events")
@SecondaryTable(name = "event_locations", pkJoinColumns = @PrimaryKeyJoinColumn(name = "event_id"))
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 120)
    private String title;

    @Column(nullable = false)
    @Size(min = 20, max = 2000)
    private String annotation;

    @Column(nullable = false)
    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "category_id", nullable = false)
    private long categoryId;

    @Column(name = "created", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "initiator_id", nullable = false)
    private Long initiatorId;

    @Column(nullable = false)
    private boolean paid;

    @Column(name = "participant_limit", nullable = false)
    private int participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation", nullable = false)
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventState state;

    @Embedded
    private Location location;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private List<Request> requests;

    @ManyToMany(mappedBy = "events")
    @ToString.Exclude
    private List<Compilation> compilations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", insertable = false, updatable = false, nullable = false)
    @ToString.Exclude
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false, nullable = false)
    @ToString.Exclude
    private Category category;

    public long getConfirmedRequests() {
        return requests.stream()
                .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                .count();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getTitle(),
                getAnnotation(),
                getDescription(),
                getCategoryId(),
                getCreatedOn(),
                getEventDate(),
                getInitiatorId(),
                isPaid(),
                getParticipantLimit(),
                getPublishedOn(),
                isRequestModeration(),
                getState());
    }
}