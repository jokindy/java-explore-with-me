package ru.practicum.explorewithme.category.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.util.PageMaker;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryPublicService {

    private final CategoryRepository categoryRepository;
    private final PageMaker<Category> pageMaker;

    public CategoryDto get(long categoryId) {
        log.debug("Getting category id: {}", categoryId);
        checkCategoryId(categoryId);
        Category category = categoryRepository.findById(categoryId).get();
        return CategoryDto.construct(category);
    }

    public List<CategoryDto> getAll(int from, int size) {
        log.debug("Getting all categories");
        List<Category> categories = categoryRepository.findAll();
        return pageMaker.getPage(from, size, categories).getContent().stream()
                .map(CategoryDto::construct)
                .collect(Collectors.toList());
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
