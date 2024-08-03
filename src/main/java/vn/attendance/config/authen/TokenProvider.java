package vn.attendance.config.authen;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.attendance.config.JwtConfig;
import vn.attendance.config.authen.service.UserDetailsServiceImpl;
import vn.attendance.util.DataUtil;

import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    @Qualifier("jwtConfigAuth")
    private JwtConfig jwtConfig;

    public String createTokenForNewUser(String username, String password) {
        return Jwts.builder()
                .setSubject(username)
                .claim("password", password)
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();
    }

    public String createToken(String username, String password) {
        try {
            Authentication auth = //
                    authenticationManager //
                    .authenticate( //
                            new UsernamePasswordAuthenticationToken( //
                                    username, //
                                    password//
                            ) //
                    );
            long now = DataUtil.getDate().getTime();

            return Jwts.builder()
                    .setSubject(auth.getName())
                    // Convert to list of strings.
                    // This is important because it affects the way we get them back in the Gateway.
                    .claim("authorities", auth.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .setIssuedAt(DataUtil.getDate())
                    .setExpiration(new Date(DataUtil.getDate().getTime() + jwtConfig.getExpiration() * 1000L))  // in milliseconds
                    .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                    .compact();
        } catch (Exception e) {
            return "";
        }
    }

    public String generateTokenFromUsername(String username) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            long now = System.currentTimeMillis();
            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .claim("authorities", userDetails.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .setIssuedAt(DataUtil.getDate())
                    .setExpiration(new Date(DataUtil.getDate().getTime() + jwtConfig.getExpiration() * 1000L))
                    .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(authToken);
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
}
