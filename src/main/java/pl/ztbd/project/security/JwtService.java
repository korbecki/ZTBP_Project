package pl.ztbd.project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


    private static final String TOKEN_SECRET = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token, boolean allowExpiration) {
        return extractClaim(token, Claims::getSubject, allowExpiration);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, false);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean allowExpiration) {
        final Claims claims = extractAllClaims(token, allowExpiration);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, boolean allowExpiration) {
        Claims claims = null;
        try {
            claims = Jwts
                    .parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            if (allowExpiration)
                claims = e.getClaims();
            else
                throw e;
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    public boolean checkBearerPrefix(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    public String generateToken(String userName) {
        Map<String, Object> claims = Map.of();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSecretKey(), SignatureAlgorithm.HS512).compact();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(TOKEN_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}