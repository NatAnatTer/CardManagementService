package ru.project.CardManagementService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.UserDto;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Сервис для реализации логики работы с JWT токеном
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    /**
     * Метод извлечения логина пользователя из JWT токена
     *
     * @param token JWT токен
     * @return String логин пользователя
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Метод генерации JWT токена
     *
     * @param userDetails объект с информацией о пользвоателе {@link UserDetails}
     * @return String токен
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof UserDto customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("role", customUserDetails.getRoles());
        }
        return generateToken(claims, userDetails);
    }

    /**
     * метод проверки валидности токена
     *
     * @param token       токен
     * @param userDetails объект с информацией о пользвоателе {@link UserDetails}
     * @return boolean результат проверки
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Метод извлечения всех параметров из авторизационного токена
     *
     * @param token           токен
     * @param claimsResolvers функция извлечения параметра
     * @param <T>             generic type
     * @return возвращает generic тип
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Метод генерации авторизационного токена
     *
     * @param extraClaims объект {@link Claims} - Map с параметрами пользователя
     * @param userDetails объект с информацией о пользвоателе {@link UserDetails}
     * @return String токен
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Метод проверки валидности токена по сроку действия
     *
     * @param token токен
     * @return результат проверки
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Метод извлечения срока действия из токена
     *
     * @param token токен
     * @return извлеченная дата
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Метод извлечения параметров из токена
     *
     * @param token токен
     * @return объект {@link Claims} - Map с извлеченными параметрами
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Метод получения ключа шифрования
     *
     * @return объект {@link Key}
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
