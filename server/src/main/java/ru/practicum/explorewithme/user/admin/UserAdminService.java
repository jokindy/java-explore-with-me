package ru.practicum.explorewithme.user.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    public User save(User user) {
        log.debug("Saving new user to db: {}", user);
        userRepository.save(user);
        return user;
    }

    public List<User> getUsers(Long[] ids, int from, int size) {
        log.debug("Getting users id: {}", (Object) ids);
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").ascending());
        if (ids == null) {
            return userRepository.findAll(pageable).getContent();
        } else {
            return userRepository.findAllByIds(ids, pageable);
        }
    }

    public void delete(long userId) {
        log.debug("Deleting user id: {}", userId);
        checkUserId(userId);
        userRepository.deleteById(userId);
    }

    public void checkUserId(long userId) {
        boolean isExists = userRepository.existsById(userId);
        if (!isExists) {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(userId, User.class.getSimpleName());
        }
    }
}
