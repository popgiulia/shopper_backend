package shopper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shopper.backend.dtos.constraints.RoleCreationDto;
import shopper.backend.dtos.responses.RoleResponseDto;
import shopper.backend.models.RoleModel;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "users", ignore = true)
    RoleModel toModel(RoleResponseDto roleResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    RoleModel toModel(RoleCreationDto roleCreationDto);
    RoleResponseDto toDto(RoleModel roleModel);
    RoleCreationDto toDto(String name);
}
