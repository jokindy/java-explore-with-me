package ru.practicum.explorewithme.category.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.util.Mapper;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryPublicManager {

    private final Mapper mapper;
    private final CategoryPublicService categoryPublicService;

    public List<CategoryDto> getCategories(@Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        List<Category> categories = categoryPublicService.getAll(from, size);
        return mapper.mapList(categories, CategoryDto.class);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {
        Category category = categoryPublicService.get(catId);
        return mapper.map(category, CategoryDto.class);
    }
}
