package shopper.backend.dtos.constraints;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UserDto {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
}
