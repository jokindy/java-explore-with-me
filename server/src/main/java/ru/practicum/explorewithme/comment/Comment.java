package ru.practicum.explorewithme.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.comment.rating.CommentRating;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "comments")
@NoArgsConstructor
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author_id", nullable = false)
    private long authorId;

    @Column(name = "event_id", nullable = false)
    private long eventId;

    @Column(nullable = false)
    @Size(min = 20, max = 2000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private boolean positive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentModerationStatus state;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private User author;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private Event event;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @ToString.Exclude
    private Set<CommentRating> commentRatings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return getId() == comment.getId()
                && getAuthorId() == comment.getAuthorId()
                && getEventId() == comment.getEventId()
                && isPositive() == comment.isPositive()
                && Objects.equals(getContent(), comment.getContent())
                && Objects.equals(getCreated(), comment.getCreated())
                && getState() == comment.getState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAuthorId(), getEventId(), getContent(),
                getCreated(), isPositive(), getState());
    }
}