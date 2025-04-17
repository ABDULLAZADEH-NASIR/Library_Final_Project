package az.texnoera.library_management_system.security.utilities;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtils {
    @Value("${security.secret-key}")
    private String jwtSecret;

    // Secret key-in size-nın doğrulugunu yoxlayır
    private SecretKey getSigningKey() {
        if (jwtSecret.getBytes().length < 32) {
            throw new IllegalArgumentException("JWT Secret key must be at least 32 bytes long for HS512.");
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Yeni Jwt yaradır (User mail-ə və userin rollarına görə)
    public String generateJwtToken(String email, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000);

        JwtBuilder jwt = Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512);

        return jwt.compact();
    }

    // Jwt tokenin düzgünlüyünü yoxlayır
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // Doğrulanmış tokenin içindəki məlumatları (claims) çıxarmaq üçün istifadə olunur
    public Claims parseJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // Secret key ilə doğrulama
                .build()  // Parser-i qurur
                .parseClaimsJws(token)  // Tokeni parse edir
                .getBody();  // Claims (payload) məlumatını çıxarır
    }
}