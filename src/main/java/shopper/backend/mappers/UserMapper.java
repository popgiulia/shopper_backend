package shopper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import shopper.backend.dtos.constraints.UserCreationDto;
import shopper.backend.dtos.requests.AuthRegisterRequestDto;
import shopper.backend.dtos.responses.UserResponseDto;
import shopper.backend.models.UserModel;

import java.util.Collections;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserModel toModel(AuthRegisterRequestDto authRegisterRequestDto);
//
    @Mapping(target = "role", ignore = true)
    UserModel toModel(UserCreationDto userCreationDto);

    @Mapping(target = "role", source = "role.name")
    UserResponseDto toDto(UserModel userModel);

    default UserDetails toUserDetails(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        return User
            .withUsername(userModel.getUsername())
            .password(userModel.getPassword())
            .authorities(Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + userModel.getRole().getName().toUpperCase()))
            )
            .build();
    }
}