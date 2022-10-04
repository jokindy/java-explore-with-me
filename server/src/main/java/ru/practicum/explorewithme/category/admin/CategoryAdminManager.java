package ru.practicum.explorewithme.category.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.util.Mapper;

@Service
@AllArgsConstructor
public class CategoryAdminManager {

    private final Mapper mapper;
    private final CategoryAdminService categoryService;

    public CategoryDto postCategory(CategoryDto categoryDto) {
        Category category = mapper.map(categoryDto, Category.class);
        categoryService.save(category);
        return mapper.map(category, CategoryDto.class);

    }

    public CategoryDto patchCategory(CategoryDto categoryDto) {
        Category updatedCategory = mapper.map(categoryDto, Category.class);
        categoryService.patch(updatedCategory);
        return mapper.map(updatedCategory, CategoryDto.class);
    }

    public void deleteCategory(long catId) {
        categoryService.delete(catId);
    }
}
