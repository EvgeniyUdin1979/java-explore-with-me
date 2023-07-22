package ru.practicum.events.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Location {

    private float lat;

    private float lon;
}
