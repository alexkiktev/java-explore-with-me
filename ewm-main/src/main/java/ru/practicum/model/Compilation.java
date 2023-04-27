package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "compilations")
@NoArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    Set<Event> events;
}
