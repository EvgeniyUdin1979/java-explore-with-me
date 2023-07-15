package ru.practicum.events.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Location {
    @Id
    private long id;

    private float lat;

    private float lon;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Event event;
}
