package shopper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import shopper.backend.dtos.constraints.ClientCreationDto;
import shopper.backend.dtos.responses.ClientResponseDto;
import shopper.backend.models.ClientModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    ClientResponseDto toDto(ClientModel clientModel);

    @Mapping(source = "firstName", target = "user.firstName")
    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "password", target = "user.password")
    ClientModel toModel(ClientCreationDto clientCreationDto);
}
