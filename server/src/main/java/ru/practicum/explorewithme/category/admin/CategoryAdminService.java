package ru.practicum.explorewithme.category.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.common.CategoryPublicService;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final CategoryPublicService categoryPublicService;

    public CategoryDto save(CategoryDto dto) {
        log.debug("Saving new category to DB: {}", dto);
        Category category = CategoryDto.toDomain(dto);
        categoryRepository.save(category);
        return CategoryDto.construct(category);
    }

    public CategoryDto patch(CategoryDto dto) {
        log.debug("Updating category by: {}", dto);
        Category updatedCategory = CategoryDto.toDomain(dto);
        Category category = CategoryDto.toDomain(get(updatedCategory.getId()));
        if (category.equals(updatedCategory)) {
            log.error("UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException("Same category");
        }
        categoryRepository.save(updatedCategory);
        return CategoryDto.construct(updatedCategory);
    }

    public void delete(long categoryId) {
        log.debug("Deleting category id: {}", categoryId);
        categoryPublicService.checkCategoryId(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    public CategoryDto get(long categoryId) {
        log.debug("Getting category id: {}", categoryId);
        return categoryPublicService.get(categoryId);
    }
}
