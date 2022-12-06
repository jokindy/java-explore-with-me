package ru.practicum.explorewithme.user.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import ru.practicum.explorewithme.util.Mapper;

import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
@AllArgsConstructor
public class UserController {

    private final UserSubscriberApiManager subscriberApiManager;
    private final UserService userService;
    private final Mapper mapper;

    @PatchMapping("/{userId}/subscribe/{subId}")
    public void addSubscriber(@PathVariable long userId,
                              @PathVariable long subId) {
        subscriberApiManager.addSubscriptionRequest(userId, subId);
        userService.addSubscriber(userId, subId);
    }

    @PatchMapping("/{userId}/subscribe/{subId}/approve")
    public void confirmSubscriber(@PathVariable long userId,
                                  @PathVariable long subId) {
        userService.handleSubscription(userId, subId, true);
    }

    @PatchMapping("/{userId}/subscribe/{subId}/reject")
    public void rejectSubscriber(@PathVariable long userId,
                                 @PathVariable long subId) {
        userService.handleSubscription(userId, subId, false);
    }


    @GetMapping("/{userId}/subscribers")
    public List<UserShortDto> getSubscribers(@PathVariable long userId) {
        List<User> subscribers = userService.getSubscribers(userId);
        return mapper.mapList(subscribers, UserShortDto.class);
    }
}
