package shopper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shopper.backend.dtos.constraints.TypeCreationDto;
import shopper.backend.dtos.responses.TypeResponseDto;
import shopper.backend.models.TypeModel;

@Mapper(componentModel = "spring")
public interface TypeMapper {
    @Mapping(target = "id", ignore = true)
    TypeModel toModel(TypeCreationDto typeCreationDto);
    TypeResponseDto toDto(TypeModel typeModel);
}
