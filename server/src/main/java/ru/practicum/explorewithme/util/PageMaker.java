package ru.practicum.explorewithme.util;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageMaker<T> {

    public Page<T> getPage(int from, int size, List<T> list) {
        Pageable pageable = getPageable(from, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    private Pageable getPageable(int from, int size) {
        return PageRequest.of(from, size, Sort.by("id").descending());
    }
}
