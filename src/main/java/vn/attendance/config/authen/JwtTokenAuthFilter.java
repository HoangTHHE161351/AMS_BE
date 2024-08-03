package vn.attendance.config.authen;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.attendance.config.JwtConfig;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Users;
import vn.attendance.repository.UsersRepository;
import vn.attendance.util.DataUtils;
import vn.attendance.util.MessageCode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenAuthFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("jwtConfigAuth")
    private final JwtConfig jwtConfig;
    @Autowired
    private UsersRepository usersRepository;

    @Value("${security.jwt.token_expire}")
    private Integer tokenExpireTime;

    public JwtTokenAuthFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {
        try {
            String header = request.getHeader(jwtConfig.getHeader());
            if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
                chain.doFilter(request, response);        // If not valid, go to the next filter.
                return;
            }
            String token = header.replace(jwtConfig.getPrefix(), "");
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtConfig.getSecret().getBytes())
                        .parseClaimsJws(token)
                        .getBody();
                String username = claims.getSubject();
                if (username != null) {
                    @SuppressWarnings("unchecked")
                    List<String> authorities = (List<String>) claims.get("authorities");
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    Users users = usersRepository.findByUsername(username).get();
                    if (users.getAccessToken() != null && !DataUtils.safeEqual(users.getAccessToken(), token) && users.getTokenExpire().plusMinutes(tokenExpireTime).isBefore(LocalDateTime.now()))
                        throw new AmsException(MessageCode.TOKEN_EXPIRE);
                    users.setAccessToken(token);
                    users.setTokenExpire(LocalDateTime.now());
                    BaseUserDetailsService.USER.set(usersRepository.save(users));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
            System.out.println(response.getHeaderNames());
            System.out.println(request.getHeaderNames());

            // go to the next filter in the filter chain
            chain.doFilter(request, response);
        }catch (Exception e){
            this.buildExceptionOutput(request, response, HttpStatus.UNAUTHORIZED.name(), "Unauthorized", HttpStatus.UNAUTHORIZED.value(), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    }

    public void buildExceptionOutput(HttpServletRequest request, HttpServletResponse response,
                                     String errorMsg, String msg, Integer status, Integer responseStatus) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("\"path\": \"")
                .append(request.getRequestURL())
                .append("\"");
        sb.append(",");
        sb.append("\"error\":");
        sb.append("\"");
        sb.append(errorMsg);
        sb.append("\"");
        sb.append(",");
        sb.append("\"message\":");
        sb.append("\"");
        sb.append(msg);
        sb.append("\"");
        sb.append(",");
        sb.append("\"timestamp\":");
        sb.append(new Date().getTime());
        sb.append(",");
        sb.append("\"code\":");
        sb.append(status);
        sb.append(",");
        sb.append("\"status\":");
        sb.append(status);
        sb.append("} ");
        response.setContentType("application/json");
//		response.setContentLength(sb.toString().length());
        response.setHeader("access-control-allow-credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With, Workspaceid");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setStatus(responseStatus);
        response.getWriter().print(sb.toString());
        response.getWriter().flush();
    }
}

