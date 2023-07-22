package ru.practicum.compilations.model;

import lombok.*;
import ru.practicum.events.model.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean pinned;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "compilations_events",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    @ToString.Exclude
    private Set<Event> events;


}
