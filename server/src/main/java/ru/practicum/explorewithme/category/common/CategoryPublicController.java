package ru.practicum.explorewithme.category.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.util.Mapper;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryPublicController {

    private final CategoryPublicService categoryService;
    private final Mapper mapper;

    @GetMapping()
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.debug("CATEGORY PUBLIC CONTROLLER - getting categories");
        List<Category> categories = categoryService.getAll(from, size).getContent();
        return mapper.mapList(categories, CategoryDto.class);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.debug("CATEGORY PUBLIC CONTROLLER - getting category id: {}", catId);
        Category category = categoryService.get(catId);
        return mapper.map(category, CategoryDto.class);
    }
}
