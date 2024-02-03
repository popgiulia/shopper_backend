package shopper.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shopper.backend.constants.UserConstants;
import shopper.backend.exceptions.NotFoundException;
import shopper.backend.mappers.UserMapper;
import shopper.backend.models.UserModel;
import shopper.backend.services.LoginTokenService;
import shopper.backend.services.UserService;
import shopper.backend.utils.JwtUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final UserService userService;
    private final LoginTokenService loginTokenService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = JwtUtils.extractJwtFromCookie(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserModel userModel = this.userService.getByUsernameModel(username)
                .orElseThrow(
                    () -> new NotFoundException(UserConstants.NOT_FOUND_BY_USERNAME)
                );

            UserDetails userDetails = this.userMapper.toUserDetails(userModel);

            boolean isTokenValid = loginTokenService.getByToken(token)
                .map(t -> !t.getIsRevoked())
                .orElse(false);

            if (jwtService.isAccessTokenValid(token, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
