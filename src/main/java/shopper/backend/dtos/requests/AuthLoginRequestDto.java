package shopper.backend.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginRequestDto {
    private String email;
    private String password;
}
