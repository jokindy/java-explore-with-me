package ru.practicum.explorewithme.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Embeddable
@NoArgsConstructor
public class Location {

    @Column(name = "lat", table = "event_locations")
    private double lat;

    @Column(name = "lon", table = "event_locations")
    private double lon;
}
