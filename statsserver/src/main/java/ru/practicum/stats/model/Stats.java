package ru.practicum.stats.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "stats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "app")
    private String application;

    @Column(name = "url")
    private String requestUrl;

    @Column(name = "ip")
    private String addressIp;

    @Column(name = "created")
    private LocalDateTime requestTime;
}
