package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.exceptions.ObjectNotFoundException;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.UserService;
import ru.practicum.explorewithme.service.mappers.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll(List<Long> ids, Pageable pageable) {
        if (ids != null) {
            return userRepository.findAllById(ids).stream().map(UserMapper::toFullDto).collect(Collectors.toList());
        }

        return userRepository.findAll(pageable).stream().map(UserMapper::toFullDto).collect(Collectors.toList());
    }

    @Override
    public UserDto add(NewUserRequest user) {
        return UserMapper.toFullDto(userRepository.save(UserMapper.toModel(user)));
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto get(long id) {
        return UserMapper.toFullDto(userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User", id)));
    }
}
