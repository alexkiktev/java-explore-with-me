package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.StatusRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findByEventIdAndStatus(Long eventId, StatusRequest pending);

    List<Request> findByEventId(Long eventId);
}
