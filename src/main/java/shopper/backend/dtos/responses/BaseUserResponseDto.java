package shopper.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public abstract class BaseUserResponseDto {
    protected UUID id;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String email;
    protected String role;
}