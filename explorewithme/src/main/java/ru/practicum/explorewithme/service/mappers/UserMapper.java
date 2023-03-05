package ru.practicum.explorewithme.service.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.utils.DateUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(user.getName(), user.getId());
    }

    public static UserDto toFullDto(User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }

    public static User toModel(NewUserRequest user) {
        return new User(null, user.getEmail(), user.getName(), DateUtils.now());
    }
}
