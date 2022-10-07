package ru.practicum.explorewithme.category.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.CategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryAdminController {

    private final CategoryAdminApiManager categoryAdminApiManager;

    @PostMapping
    public CategoryDto postCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.debug("Saving new category to DB");
        return categoryAdminApiManager.postCategory(categoryDto);
    }

    @PatchMapping
    public CategoryDto patchCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.debug("Updating category id: {}", categoryDto.getId());
        return categoryAdminApiManager.patchCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public String deleteCategory(@PathVariable long catId) {
        log.debug("Deleting category id: {}", catId);
        categoryAdminApiManager.deleteCategory(catId);
        return String.format("Category id: %s is deleted", catId);
    }
}
