package ru.practicum.event.service;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.SortParam;
import ru.practicum.event.model.StateEvent;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventCriteriaQuery {

    public static Specification<Event> getFilterEventsAdmin(List<Long> users, List<StateEvent> states,
                                                            List<Long> categories, LocalDateTime rangeStart,
                                                            LocalDateTime rangeEnd) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (users != null) {
                if (users.get(0) != 0) {
                    predicates.add(root.get("initiator").get("id").in(users));
                }
            }
            if (states != null) {
                predicates.add(root.get("state").in(states));
            }
            if (categories != null) {
                if (categories.get(0) != 0) {
                    predicates.add(root.get("category").get("id").in(categories));
                }
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

    public static Specification<Event> getFilterEventsPublic(String text, List<Long> categories, Boolean paid,
                                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                             SortParam sort, StateEvent stateEvent,
                                                             Boolean onlyAvailable) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (text != null) {
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                                "%" + text.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                                "%" + text.toLowerCase() + "%")));
            }
            if (categories != null) {
                if (categories.get(0) != 0) {
                    predicates.add(root.get("category").get("id").in(categories));
                }
            }
            if (paid != null) {
                predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
            }
            if (rangeStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            }
            if (rangeEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }
            if (sort != null && sort.equals(SortParam.EVENT_DATE)) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("eventDate")));
            }
            if (sort != null && sort.equals(SortParam.VIEWS)) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("views")));
            }
            if (onlyAvailable != null && onlyAvailable) {
                predicates.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("participantLimit"), 0),
                        criteriaBuilder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"))));
            }
            predicates.add(criteriaBuilder.equal(root.get("state"), stateEvent));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
