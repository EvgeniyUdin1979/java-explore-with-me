package ru.practicum.users.model;

import lombok.*;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.model.Event;
import ru.practicum.request.model.Request;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 250)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "initiator")
    private Set<Event> events;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requester")
    private Set<Request> requests;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Set<Comment> comments;
}
