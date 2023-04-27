package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;

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
        log.info("Создан пользователь {}", createdUserDto);
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
        log.info("Список пользователей по запросу с параметрами {}", userDtos);
        return userDtos;
    }

    @Override
    public void deleteUser(Long id) {
        getUser(id);
        userRepository.deleteById(id);
        log.info("Удален пользователь id {}", id);
    }

    private void getUser(Long id) {
        userMapper.toUserDto(userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%s was not found", id))));
    }

}
