package ru.practicum.explorewithme.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.dto.validation.AdvanceInfo;
import ru.practicum.explorewithme.user.dto.validation.BasicInfo;
import ru.practicum.explorewithme.user.dto.validation.ValidEmail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @ValidEmail(groups = AdvanceInfo.class)
    @NotBlank(groups = BasicInfo.class)
    @NotNull(groups = BasicInfo.class)
    private String email;

    @NotBlank(groups = BasicInfo.class)
    @NotNull(groups = BasicInfo.class)
    private String name;

    public static UserDto construct(User user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getName());
    }

    public static User toDomain(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}