package ru.practicum.event.service;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.StateEvent;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventCriteriaQuery {

    public static Specification<Event> getFilterEventsAdmin(List<Long> users, List<StateEvent> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (users != null) {
                predicates.add(root.get("initiator").get("id").in(users));
            }
            if (states != null) {
                predicates.add(root.get("state").in(states));
            }
            if (categories != null) {
                predicates.add(root.get("category").get("id").in(categories));
            }
            if (rangeStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            }
            if (rangeEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
