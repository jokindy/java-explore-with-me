package ru.practicum.explorewithme.user.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.util.Mapper;

import java.util.List;

@Service
@AllArgsConstructor
public class UserAdminApiManager {

    private final Mapper mapper;
    private final UserAdminService userAdminService;

    public List<UserDto> getUsers(Long[] ids, int from, int size) {
        List<User> users = userAdminService.getUsers(ids, from, size);
        return mapper.mapList(users, UserDto.class);
    }

    public UserDto saveUser(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        userAdminService.save(user);
        return mapper.map(user, UserDto.class);
    }

    public void deleteUser(long userId) {
        userAdminService.delete(userId);
    }
}
