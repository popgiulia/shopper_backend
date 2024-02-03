package shopper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shopper.backend.dtos.constraints.SizeCreationDto;
import shopper.backend.dtos.responses.SizeResponseDto;
import shopper.backend.models.SizeModel;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    @Mapping(target = "id", ignore = true)
    SizeModel toModel(SizeCreationDto sizeCreationDto);
    SizeResponseDto toDto(SizeModel sizeModel);
}
