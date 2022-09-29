package ru.practicum.explorewithme.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.validation.BasicInfo;

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

    @GetMapping
    public List<UserDto> getUsers(@RequestParam Long[] ids,
                                  @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                  @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Getting users id: {}", (Object) ids);
        return userAdminService.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto saveUser(@Validated(BasicInfo.class) @RequestBody UserDto userDto) {
        log.debug("Saving new user: {}", userDto);
        return userAdminService.save(userDto);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable long userId) {
        log.debug("Deleting user id: {}", userId);
        userAdminService.delete(userId);
        return String.format("User id: %s is deleted", userId);
    }
}
