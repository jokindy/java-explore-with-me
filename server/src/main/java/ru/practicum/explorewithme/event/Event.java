package ru.practicum.explorewithme.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    private String title;
    private String annotation;
    private String description;

    @Column(name = "category_id")
    private long categoryId;

    @Column(name = "created")
    private LocalDateTime createdOn;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "initiator_id")
    private Long initiatorId;

    private boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Embedded
    private Location location;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private List<Request> requests;

    @ManyToMany(mappedBy = "events")
    @ToString.Exclude
    private Set<Compilation> compilations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id", insertable = false, updatable = false, nullable = false)
    @ToString.Exclude
    private User initiator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false, nullable = false)
    @ToString.Exclude
    private Category category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}