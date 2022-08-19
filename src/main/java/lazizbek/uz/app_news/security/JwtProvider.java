package lazizbek.uz.app_news.security;

import io.jsonwebtoken.SignatureAlgorithm;
import lazizbek.uz.app_news.entity.Role;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.util.Date;

@Component
public class JwtProvider {
    private final long expiredTime = 1_000_000;
    private final String secret = "secretKey";

    // generate jwt token
    public String generateToken(String username, Role role) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .claim("role", role.getName())
                .compact();
    }

    // get username from token
    public String getUsernameFromToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
