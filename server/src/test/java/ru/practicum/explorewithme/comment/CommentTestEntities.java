package ru.practicum.explorewithme.comment;

import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.request.RequestStatus;
import ru.practicum.explorewithme.user.User;

import java.time.LocalDateTime;

public class CommentTestEntities {

    public static User getUser() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setName("user");
        return user;
    }

    public static User getAuthor() {
        User user = new User();
        user.setEmail("author@mail.com");
        user.setName("author");
        return user;
    }

    public static Event getEvent() {
        Event event = new Event();
        event.setTitle("Test title");
        event.setAnnotation("Test annotation for 20 characters");
        event.setDescription("Test description for 20 characters");
        event.setCategoryId(1L);
        event.setInitiatorId(1L);
        event.setCreatedOn(LocalDateTime.now());
        event.setEventDate(LocalDateTime.now().minusMonths(2));
        event.setPaid(false);
        event.setParticipantLimit(12);
        event.setRequestModeration(false);
        event.setState(EventState.PUBLISHED);
        return event;
    }

    public static Category getCategory() {
        Category category = new Category();
        category.setName("Test category");
        return category;
    }

    public static Comment getComment(long authorId, long eventId) {
        Comment comment = new Comment();
        comment.setAuthorId(authorId);
        comment.setEventId(eventId);
        comment.setContent("Test comment to event");
        comment.setState(CommentModerationStatus.PENDING);
        comment.setPositive(true);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static Request getRequest() {
        Request request = new Request();
        request.setRequesterId(2L);
        request.setStatus(RequestStatus.CONFIRMED);
        request.setEventId(1L);
        request.setCreated(LocalDateTime.now());
        return request;
    }
}
