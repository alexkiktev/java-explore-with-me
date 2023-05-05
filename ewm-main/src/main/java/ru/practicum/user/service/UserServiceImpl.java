package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserDto createdUserDto = userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
        log.info("Created user {}", createdUserDto);
        return createdUserDto;
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        List<UserDto> userDtos = new ArrayList<>();
        Pageable pageParams = PageRequest.of(from / size, size);
        if (ids == null) {
            userRepository.findAll(pageParams).forEach(u -> userDtos.add(userMapper.toUserDto(u)));
        } else {
            userRepository.findAllByIdIsIn(ids, pageParams).forEach(u -> userDtos.add(userMapper.toUserDto(u)));
        }
        log.info("List of users on request with parameters {}", userDtos);
        return userDtos;
    }

    @Override
    public void deleteUser(Long id) {
        getUser(id);
        userRepository.deleteById(id);
        log.info("Deleted user id {}", id);
    }

    private void getUser(Long id) {
        userMapper.toUserDto(userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%s was not found", id))));
    }

}
