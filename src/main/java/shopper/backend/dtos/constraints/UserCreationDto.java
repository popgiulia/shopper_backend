package shopper.backend.dtos.constraints;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationDto extends UserDto {
    protected String role;
}
