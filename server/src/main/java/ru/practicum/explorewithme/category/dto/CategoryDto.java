package ru.practicum.explorewithme.category.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @NotNull(message = "Name can't be null")
    @Length(min = 5, max = 120, message = "Name must be 5 characters long min and 120 max")
    private String name;
}
