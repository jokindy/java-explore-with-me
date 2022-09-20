package ru.practicum.explorewithme.category.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryRepo;
import ru.practicum.explorewithme.category.common.CategoryPublicService;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryAdminService {

    private final CategoryRepo categoryRepo;
    private CategoryPublicService categoryPublicService;

    public Category save(Category category) {
        log.debug("CATEGORY ADMIN SERVICE - saving new category to DB: {}", category);
        return categoryRepo.save(category);
    }

    public void patch(Category updatedCategory) {
        log.debug("CATEGORY ADMIN SERVICE - updating category by: {}", updatedCategory);
        Category category = get(updatedCategory.getId());
        if (category.equals(updatedCategory)) {
            log.warn("CATEGORY ADMIN SERVICE - UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException("Same category");
        }
        categoryRepo.save(updatedCategory);
    }

    public void delete(long categoryId) {
        log.debug("CATEGORY ADMIN SERVICE - deleting category id: {}", categoryId);
        categoryPublicService.checkCategoryId(categoryId);
        categoryRepo.deleteById(categoryId);
    }

    public Category get(long categoryId) {
        log.debug("CATEGORY ADMIN SERVICE - getting category id: {}", categoryId);
        return categoryPublicService.get(categoryId);
    }
}
