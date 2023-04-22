package ru.practicum.service;

import ru.practicum.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);

    void deleteUser(Long id);

}
