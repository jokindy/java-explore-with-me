package ru.practicum.explorewithme.category.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryRepo;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.util.PageMaker;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryPublicService {

    private final CategoryRepo categoryRepo;
    private final PageMaker<Category> pageMaker;

    public Category get(long categoryId) {
        log.debug("CATEGORY PUBLIC SERVICE - getting category id: {}", categoryId);
        checkCategoryId(categoryId);
        return categoryRepo.findById(categoryId).get();
    }

    public Page<Category> getAll(int from, int size) {
        log.debug("CATEGORY PUBLIC SERVICE - getting all categories");
        List<Category> categories = categoryRepo.findAll();
        return pageMaker.getPage(from, size, categories);
    }

    public void checkCategoryId(long categoryId) {
        log.debug("CATEGORY PUBLIC SERVICE - checking category id: {} is exists", categoryId);
        boolean isExists = categoryRepo.existsById(categoryId);
        if (!isExists) {
            log.warn("CATEGORY PUBLIC SERVICE - ModelNotFoundException");
            throw new ModelNotFoundException(categoryId, Category.class.getSimpleName());
        }
    }
}
