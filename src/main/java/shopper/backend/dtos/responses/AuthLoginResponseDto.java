package shopper.backend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthLoginResponseDto {
    String accessToken;
    String refreshToken;
}
