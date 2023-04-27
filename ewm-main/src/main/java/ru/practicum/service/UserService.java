package ru.practicum.service;

import ru.practicum.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    void deleteUser(Long id);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

}
