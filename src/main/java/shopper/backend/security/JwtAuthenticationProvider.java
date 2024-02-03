package shopper.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import shopper.backend.constants.UserConstants;
import shopper.backend.exceptions.JwtAuthenticationException;
import shopper.backend.exceptions.NotFoundException;
import shopper.backend.mappers.UserMapper;
import shopper.backend.models.UserModel;
import shopper.backend.services.UserService;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            throw new JwtAuthenticationException("Invalid authentication class not instance of UsernamePasswordAuthenticationToken");
        }

        String token = (String) authentication.getCredentials();
        String username = jwtService.extractUsername(token);

        UserModel userModel = this.userService.getByUsernameModel(username).orElseThrow(
            () -> new NotFoundException(UserConstants.NOT_FOUND_BY_USERNAME)
        );

        UserDetails userDetails = this.userMapper.toUserDetails(userModel);

        if (userDetails == null) {
            throw new NotFoundException("User details not found with the provided token");
        }

        if(!jwtService.isAccessTokenValid(token, userDetails)) {
            throw new JwtAuthenticationException("Invalid JWT token");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}