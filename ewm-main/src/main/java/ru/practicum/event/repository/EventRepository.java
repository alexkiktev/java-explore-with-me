package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.StateEvent;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findByInitiatorId(Long userId, Pageable pageParams);

    Page<Event> findAll(Specification<Event> eventCriterias, Pageable pageParams);

    Event findEventByIdAndState(Long id, StateEvent published);

    List<Event> findAllByIdIn(List<Long> events);

}
