package nest.esprit.user.Configuration.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import nest.esprit.user.Configuration.Provider.TokenProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import org.apache.commons.lang3.StringUtils;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static nest.esprit.user.Utils.ExceptionUtils.processError;
import org.springframework.beans.factory.annotation.Value;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private static final String HTTP_OPTIONS_METHOD = "OPTIONS";
   private static final String TOKEN_KEY = "token";
    private final TokenProvider tokenProvider;
    protected static final String EMAIL_KEY = "email";
    private static final String TOKEN_PREFIX ="Bearer ";
    @Value("${server.servlet.context-path}")
    private static String contextPath;
    private static final String[] PUBLIC_ROUTES ={"/user/login","/user/register","/user/register","/user/refresh/token","user/addTutor"};




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        try {
            Map<String,String> values=getRequestValues(request);
            String token = getToken(request);

            if (tokenProvider.isTokenValid(values.get(EMAIL_KEY), token)) {
                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
                Authentication authentication = tokenProvider.getAuthentication(values.get(EMAIL_KEY), authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
            filter.doFilter(request, response);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            // exception.printStackTrace();
            processError(request, response, exception);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(AUTHORIZATION) == null || !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) ||
                request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
    }



    private String getToken(HttpServletRequest request) {
        return ofNullable(request.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(token -> token.replace(TOKEN_PREFIX, StringUtils.EMPTY)).get();
    }
    private Map<String, String> getRequestValues(HttpServletRequest request) {
        return Map.of(EMAIL_KEY,tokenProvider.getSubject(getToken(request),request),TOKEN_KEY,getToken(request));
    }

}
