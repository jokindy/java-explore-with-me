package ru.practicum.explorewithme.category.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.CategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryPublicController {

    private final CategoryPublicService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.debug("Getting categories");
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.debug("Getting category id: {}", catId);
        return categoryService.get(catId);
    }
}
