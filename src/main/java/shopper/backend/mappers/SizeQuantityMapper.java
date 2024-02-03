package shopper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shopper.backend.dtos.responses.SizeQuantityResponseDto;
import shopper.backend.models.SizeQuantityModel;

@Mapper(componentModel = "spring")
public interface SizeQuantityMapper {
    @Mapping(target = "size", source = "size.name")
    @Mapping(target = "product", source = "product.name")
    SizeQuantityResponseDto toDto(SizeQuantityModel sizeQuantityModel);
}
