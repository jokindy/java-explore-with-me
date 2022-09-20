package ru.practicum.explorewithme.category.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.dto.CategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryAdminController {

    private final ModelMapper modelMapper;
    private final CategoryAdminService categoryService;

    @PostMapping()
    public CategoryDto postCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.debug("CATEGORY ADMIN CONTROLLER - saving new category to DB");
        Category category = modelMapper.map(categoryDto, Category.class);
        category = categoryService.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    @PatchMapping()
    public CategoryDto patchCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.debug("CATEGORY ADMIN CONTROLLER - updating category id: {}", categoryDto.getId());
        Category updatedCategory = modelMapper.map(categoryDto, Category.class);
        categoryService.patch(updatedCategory);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @DeleteMapping("/{catId}")
    public String deleteCategory(@PathVariable long catId) {
        log.debug("CATEGORY ADMIN CONTROLLER - deleting category id: {}", catId);
        categoryService.delete(catId);
        return String.format("Category id: %s is deleted", catId);
    }
}
