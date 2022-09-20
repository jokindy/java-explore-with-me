package ru.practicum.explorewithme.request.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.request.Request;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper extends ModelMapper {

    public ParticipantRequestDto map(Request request) {
        ParticipantRequestDto requestDto = super.map(request, ParticipantRequestDto.class);
        requestDto.setRequester(request.getRequesterId());
        requestDto.setEvent(request.getEventId());
        return requestDto;
    }

    public List<ParticipantRequestDto> mapList(List<Request> list) {
        return list.stream()
                .map(request -> super.map(request, ParticipantRequestDto.class))
                .collect(Collectors.toList());
    }
}
