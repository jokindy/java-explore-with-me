package ru.practicum.explorewithme.category.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.common.CategoryPublicService;
import ru.practicum.explorewithme.exception.ForbiddenException;


@Slf4j
@Service
@AllArgsConstructor
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final CategoryPublicService categoryPublicService;

    public Category save(Category category) {
        log.debug("Saving new category to DB: {}", category);
        categoryRepository.save(category);
        return category;
    }

    public void patch(Category updatedCategory) {
        log.debug("Updating category by: {}", updatedCategory);
        Category category = categoryPublicService.get(updatedCategory.getId());
        if (category.equals(updatedCategory)) {
            log.error("UpdateIsForbiddenException");
            throw ForbiddenException.updateSameCategory(category.getId());
        }
        categoryRepository.save(updatedCategory);
    }

    public void delete(long categoryId) {
        log.debug("Deleting category id: {}", categoryId);
        categoryPublicService.checkCategoryId(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
