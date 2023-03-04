package ru.practicum.explorewithme.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventSort;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.repository.EventSearchRepository;
import ru.practicum.explorewithme.utils.DateUtils;
import ru.practicum.explorewithme.utils.TimePeriod;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventSearchRepositoryImpl implements EventSearchRepository {
    private final EntityManager entityManager;

    @Override
    public List<Event> search(String text, List<Long> categories, Boolean paid, TimePeriod period, Boolean onlyAvailable, EventSort sort, Pageable pageable) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(Event.class);
        var root = query.from(Event.class);

        var predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("state"), EventState.PUBLISHED));

        if (text != null) {
            predicates.add(
                    cb.or(
                            cb.like(cb.upper(root.get("annotation")), "%" + text.toUpperCase() + "%"),
                            cb.like(cb.upper(root.get("description")), "%" + text.toUpperCase() + "%")
                    )
            );
        }

        if (categories != null && !categories.isEmpty()) {
            predicates.add(root.get("category").in(categories));
        }

        if (paid != null) {
            predicates.add(cb.equal(root.get("paid"), paid));
        }

        if (Boolean.TRUE.equals(onlyAvailable)) {
            predicates.add(
                    cb.or(
                            cb.le(root.get("confirmedRequest"), root.get("participantLimit")),
                            cb.le(root.get("participantLimit"), 0)
                    )
            );
        }

        if (period != null) {
            if (period.getStart() != null) {
                predicates.add(cb.greaterThan(root.get("eventDate"), period.getStart()));
            }
            if (period.getEnd() != null) {
                predicates.add(cb.lessThan(root.get("eventDate"), period.getEnd()));
            }
        } else {
            predicates.add(cb.greaterThan(root.get("eventDate"), DateUtils.now()));
        }

        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    query.orderBy(cb.desc(root.get("eventDate")));
                    break;
                case VIEWS:
                    // обработка будет выше
                    break;
                default:
                    throw new IllegalArgumentException("'sort' have illegal value: " + sort);
            }
        }

        //noinspection SuspiciousToArrayCall
        query.where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(query)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<Event> search(List<Long> users, List<EventState> states, List<Long> categories, TimePeriod period, Pageable pageable) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(Event.class);
        var root = query.from(Event.class);

        var predicates = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            predicates.add(root.get("initiator").in(users));
        }

        if (states != null && !states.isEmpty()) {
            predicates.add(root.get("state").in(states));
        }

        if (categories != null && !categories.isEmpty()) {
            predicates.add(root.get("category").in(categories));
        }

        if (period != null) {
            if (period.getStart() != null) {
                predicates.add(cb.greaterThan(root.get("eventDate"), period.getStart()));
            }
            if (period.getEnd() != null) {
                predicates.add(cb.lessThan(root.get("eventDate"), period.getEnd()));
            }
        } else {
            predicates.add(cb.greaterThan(root.get("eventDate"), DateUtils.now()));
        }

        //noinspection SuspiciousToArrayCall
        query.where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(query)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
