package code.travelplanner.Backend.user.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String securityKey;

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    /* Generate token
    * Done when user logs in */
    public String generateToken (Long userId, String username) {
        return Jwts.builder()
                .subject(String.valueOf(userId)) // Who has logged in
                .claim("name", username) // Custom claim - name to pass along with token
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey()) // Sign with secret key
                .compact();
    }

    /* Extract userId from token
    * Called on every request requiring authentication */
    public Long extractUserId(String token) {
        String subject = extractClaims(token).getSubject();
        return Long.parseLong(subject);
    }

    /* Validate token
    * Checks for expired and incorrectly signed tokens
     */
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;

        } catch (SecurityException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Verify signature
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] KeyBytes = Decoders.BASE64.decode(securityKey);
        return Keys.hmacShaKeyFor(KeyBytes);
    }
}
