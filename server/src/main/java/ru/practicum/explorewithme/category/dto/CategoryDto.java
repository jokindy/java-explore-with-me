package ru.practicum.explorewithme.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explorewithme.category.Category;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;

    @NotNull(message = "Name can't be null")
    @Length(min = 5, max = 120, message = "Name must be 5 characters long min and 120 max")
    private String name;

    public static CategoryDto construct(Category category) {
        return new CategoryDto(category.getId(),
                category.getName());
    }

    public static Category toDomain(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}
