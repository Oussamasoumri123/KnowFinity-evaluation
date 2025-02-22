package nest.esprit.user.Configuration.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import nest.esprit.user.Controller.HttpResponseUser;

import java.io.IOException;
import java.io.OutputStream;

import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpResponseUser httpResponseUser = HttpResponseUser.builder()
                .timeStamp(now().toString())
                .reason(authException.getMessage()+" you need to login To Access this resource ")
                .status(UNAUTHORIZED)
                .statusCode(UNAUTHORIZED.value())
                .build();
        response.setContentType("application/json");
        response.setStatus(UNAUTHORIZED.value());
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out , httpResponseUser);
        out.flush();
    }
}
