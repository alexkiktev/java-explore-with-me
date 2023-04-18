package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "hits")
@NoArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String app;
    @Column(nullable = false, length = 50)
    private String uri;
    @Column(nullable = false, length = 15)
    private String ip;
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
