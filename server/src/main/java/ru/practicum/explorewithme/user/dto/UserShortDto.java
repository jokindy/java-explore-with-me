package ru.practicum.explorewithme.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {

    private Long id;
    private String name;

    public static UserShortDto construct(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
