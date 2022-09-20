package ru.practicum.explorewithme.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.util.PageMaker;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserAdminService {

    private final UserRepo userRepo;
    private final PageMaker<User> pageMaker;

    public User save(User user) {
        return userRepo.save(user);
    }

    public Page<User> getUsers(Long[] ids, int from, int size) {
        log.debug("USER ADMIN SERVICE - getting users id: {}", (Object) ids);
        List<Long> longList = Arrays.asList(ids);
        List<User> users = longList.stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return pageMaker.getPage(from, size, users);
    }


    public User get(long userId) {
        log.debug("USER ADMIN SERVICE - getting user id: {}", userId);
        Optional<User> userOptional = userRepo.findById(userId);
        return userOptional.orElse(null);
    }

    public void delete(long userId) {
        log.debug("USER ADMIN SERVICE - deleting user id: {}", userId);
        checkUserId(userId);
        userRepo.deleteById(userId);
    }

    public void checkUserId(long userId) {
        boolean isExists = userRepo.existsById(userId);
        if (!isExists) {
            log.warn("USER ADMIN SERVICE - ModelNotFoundException");
            throw new ModelNotFoundException(userId, User.class.getSimpleName());
        }
    }
}
