package ru.practicum.explorewithme.category.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.exception.ModelNotFoundException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryPublicService {

    private final CategoryRepository categoryRepository;

    public Category get(long categoryId) {
        log.debug("Getting category id: {}", categoryId);
        checkCategoryId(categoryId);
        return categoryRepository.findById(categoryId).get();
    }

    public List<Category> getAll(int from, int size) {
        log.debug("Getting all categories");
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").ascending());
        return categoryRepository.findAll(pageable).getContent();
    }

    public void checkCategoryId(long categoryId) {
        log.debug("Checking category id: {} is exists", categoryId);
        boolean isExists = categoryRepository.existsById(categoryId);
        if (!isExists) {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(categoryId, Category.class.getSimpleName());
        }
    }
}