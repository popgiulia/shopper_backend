package shopper.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shopper.backend.constants.JwtConstants;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long accessExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", JwtConstants.TOKEN_TYPE_ACCESS);
        return buildToken(claims, userDetails, accessExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", JwtConstants.TOKEN_TYPE_REFRESH);
        return buildToken(claims, userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private boolean isTokenValid(String token, UserDetails userDetails, String tokenType) {
        final String username = extractUsername(token);
        final String type = extractClaim(token, claims -> claims.get("token_type", String.class));
        return username.equals(userDetails.getUsername()) && type.equals(tokenType) && isTokenNotExpired(token);
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        final String type = extractClaim(token, claims -> claims.get("token_type", String.class));
        return username.equals(userDetails.getUsername()) && type.equals(JwtConstants.TOKEN_TYPE_ACCESS) && isTokenNotExpired(token);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        final String type = extractClaim(token, claims -> claims.get("token_type", String.class));
        return username.equals(userDetails.getUsername()) && type.equals(JwtConstants.TOKEN_TYPE_REFRESH) && isTokenNotExpired(token);
    }

    private boolean isTokenNotExpired(String token) {
        return extractExpiration(token).after(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}