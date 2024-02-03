package shopper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shopper.backend.dtos.constraints.CategoryCreationDto;
import shopper.backend.dtos.responses.CategoryResponseDto;
import shopper.backend.models.CategoryModel;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    //Se utilizează pentru a transforma datele primite de la nivelul aplicației într-un model care poate fi stocat în baza de date.
    @Mapping(target = "id", ignore = true)
    CategoryModel toModel(CategoryCreationDto categoryCreationDto);
    CategoryResponseDto toDto(CategoryModel categoryModel);
}
