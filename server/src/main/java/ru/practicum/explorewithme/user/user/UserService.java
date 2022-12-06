package ru.practicum.explorewithme.user.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.subscribers.SubscribeRequest;
import ru.practicum.explorewithme.user.subscribers.SubscriberRepository;
import ru.practicum.explorewithme.user.subscribers.SubscriberStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static ru.practicum.explorewithme.user.subscribers.SubscriberStatus.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubscriberRepository subscriberRepository;

    @Transactional
    public void addSubscriber(long userId, long subId) {
        if (userId == subId) {
            throw new RuntimeException();
        }
        SubscribeRequest request = new SubscribeRequest();
        request.setUserId(userId);
        request.setSubscriberId(subId);
        request.setStatus(PENDING);
        subscriberRepository.save(request);
    }

    @Transactional
    public void handleSubscription(long userId, long subId, boolean isApproved) {
        if (userId == subId) {
            throw new RuntimeException();
        }
        SubscriberStatus status = isApproved ? APPROVED : REJECTED;
        userRepository.handleSubscriber(userId, subId, status);
    }

    public List<User> getSubscribers(long userId) {
        Optional<User> userO = userRepository.findById(userId);
        if (userO.isPresent()) {
            return userO.get().getSubscribers();
        } else {
            throw new ModelNotFoundException(userId, User.class.getSimpleName());
        }
    }
}
