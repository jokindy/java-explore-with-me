package ru.practicum.explorewithme.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.user.dto.UserDto;
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

    private final UserRepository userRepository;
    private final PageMaker<User> pageMaker;

    public UserDto save(UserDto dto) {
        User user = UserDto.toDomain(dto);
        userRepository.save(user);
        return UserDto.construct(user);
    }

    public List<UserDto> getUsers(Long[] ids, int from, int size) {
        log.debug("Getting users id: {}", (Object) ids);
        List<Long> longList = Arrays.asList(ids);
        List<User> users = longList.stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return pageMaker.getPage(from, size, users).stream()
                .map(UserDto::construct)
                .collect(Collectors.toList());
    }


    public User get(long userId) {
        log.debug("Getting user id: {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
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
