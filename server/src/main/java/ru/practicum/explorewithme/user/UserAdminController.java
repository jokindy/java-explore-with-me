package ru.practicum.explorewithme.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.validation.BasicInfo;
import ru.practicum.explorewithme.util.Mapper;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;
    private final Mapper mapper;

    @GetMapping()
    public List<UserDto> getUsers(@RequestParam Long[] ids,
                                  @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                  @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("USER ADMIN CONTROLLER - Getting users id: {}", (Object) ids);
        List<User> users = userAdminService.getUsers(ids, from, size).getContent();
        return mapper.mapList(users, UserDto.class);
    }

    @PostMapping()
    public UserDto saveUser(@Validated(BasicInfo.class) @RequestBody UserDto userDto) {
        log.debug("USER ADMIN CONTROLLER - saving new user: {}", userDto);
        User user = mapper.map(userDto, User.class);
        userAdminService.save(user);
        return mapper.map(user, UserDto.class);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable long userId) {
        log.debug("USER ADMIN CONTROLLER - deleting user id: {}", userId);
        userAdminService.delete(userId);
        return String.format("User id: %s is deleted", userId);
    }
}
