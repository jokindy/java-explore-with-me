package ru.practicum.explorewithme.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Column(nullable = false)
    private int useful;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private Event event;

    public static Comment toEntity(NewCommentDto dto, long userId, long eventId) {
        Comment comment = new Comment();
        comment.setAuthorId(userId);
        comment.setEventId(eventId);
        comment.setPositive(dto.getPositive());
        comment.setContent(dto.getContent());
        comment.setCreated(dto.getCreated());
        comment.setState(dto.getState());
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return getId() == comment.getId()
                && getAuthorId() == comment.getAuthorId()
                && getEventId() == comment.getEventId()
                && isPositive() == comment.isPositive()
                && getUseful() == comment.getUseful()
                && Objects.equals(getContent(), comment.getContent())
                && Objects.equals(getCreated(), comment.getCreated())
                && getState() == comment.getState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAuthorId(), getEventId(), getContent(),
                getCreated(), isPositive(), getUseful(), getState());
    }
}