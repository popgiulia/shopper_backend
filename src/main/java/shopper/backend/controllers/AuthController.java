package shopper.backend.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shopper.backend.constants.AuthConstants;
import shopper.backend.dtos.requests.AuthLoginRequestDto;
import shopper.backend.dtos.requests.AuthRegisterRequestDto;
import shopper.backend.dtos.responses.AuthLoginResponseDto;
import shopper.backend.dtos.responses.UserResponseDto;
import shopper.backend.responses.MessageResponsePayload;
import shopper.backend.security.LogoutService;
import shopper.backend.services.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    // Valorile configurate pentru expirarea token-urilor JWT.
    @Value("${application.security.jwt.expiration}")
    private long accessExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    // Serviciile necesare pentru gestionarea autentificării și deconectării.
    private final AuthService authService;
    private final LogoutService logoutService;

    // Endpoint pentru înregistrare (creare de cont).
    @PostMapping("/register")
    public ResponseEntity<MessageResponsePayload> register(
        @Validated @RequestBody AuthRegisterRequestDto authRegisterRequestDto
    ) {
        authService.register(authRegisterRequestDto);
        MessageResponsePayload responsePayload = MessageResponsePayload.success(AuthConstants.REGISTERED_SUCCESS);
        return ResponseEntity.ok(responsePayload);
    }

    // Endpoint pentru autentificare.
    @PostMapping("/login")
    public ResponseEntity<MessageResponsePayload> login(
        @Validated @RequestBody AuthLoginRequestDto authLoginRequestDto,
        HttpServletResponse response
    ) {
        // Se realizează autentificarea și se obțin token-urile de acces și reîmprospătare.
        AuthLoginResponseDto authLoginResponseDto = authService.login(authLoginRequestDto);

        // Se adaugă cookie-uri pentru token-urile de acces și reîmprospătare în răspunsul HTTP.
        Cookie accessTokenCookie = new Cookie("accessToken", authLoginResponseDto.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) (accessExpiration / 1000));
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", authLoginResponseDto.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (refreshExpiration / 1000));
        response.addCookie(refreshTokenCookie);

        // Se returnează un mesaj de succes în răspunsul HTTP.
        MessageResponsePayload responsePayload = MessageResponsePayload.success(
            AuthConstants.LOGGED_IN_SUCCESS
        );

        return ResponseEntity.ok(responsePayload);
    }

    // Endpoint pentru obținerea informațiilor despre utilizatorul autentificat.
    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> getLoggedUser() {
        UserResponseDto responsePayload = authService.getLoggedUser();
        return ResponseEntity.ok(responsePayload);
    }
}
