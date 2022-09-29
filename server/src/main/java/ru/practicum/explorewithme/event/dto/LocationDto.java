package ru.practicum.explorewithme.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private double lat;
    private double lon;

    public static LocationDto construct(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public static Location toDomain(LocationDto dto) {
        Location location = new Location();
        location.setLat(dto.getLat());
        location.setLon(dto.getLon());
        return location;
    }
}
