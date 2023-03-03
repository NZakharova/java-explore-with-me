package ru.practicum.explorewithme.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;

import java.util.List;

public interface UserService {
    List<UserShortDto> getAll(List<Long> ids, Pageable pageable);

    UserDto add(NewUserRequest user);

    void delete(long id);

    UserDto get(long id);
}
