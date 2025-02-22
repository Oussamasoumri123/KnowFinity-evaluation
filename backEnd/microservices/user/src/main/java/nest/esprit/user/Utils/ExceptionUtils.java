package nest.esprit.user.Utils;

import com.auth0.jwt.exceptions.InvalidClaimException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import nest.esprit.user.Controller.HttpResponseUser;
import nest.esprit.user.Exception.ApiException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.io.OutputStream;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class ExceptionUtils {

    public static void processError(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        if(exception instanceof ApiException || exception instanceof DisabledException || exception instanceof LockedException ||
                exception instanceof BadCredentialsException || exception instanceof InvalidClaimException) {
            HttpResponseUser httpResponseUser = getHttpResponse(response, exception.getMessage(), BAD_REQUEST);
            writeResponse(response, httpResponseUser);
        } else if (exception instanceof TokenExpiredException) {
            HttpResponseUser httpResponseUser = getHttpResponse(response, exception.getMessage(), UNAUTHORIZED);
            writeResponse(response, httpResponseUser);
        } else {
            HttpResponseUser httpResponseUser = getHttpResponse(response, "An error occurred. Please try again.", INTERNAL_SERVER_ERROR);
            writeResponse(response, httpResponseUser);
        }
        log.error(exception.getMessage());
    }
    private static void writeResponse(HttpServletResponse response, HttpResponseUser httpResponseUser) {
        OutputStream out;
        try{
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, httpResponseUser);
            out.flush();
        }catch (Exception exception) {
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static HttpResponseUser getHttpResponse(HttpServletResponse response, String message, HttpStatus httpStatus) {
        HttpResponseUser httpResponseUser = HttpResponseUser.builder()
                .timeStamp(now().toString())
                .reason(message)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        return httpResponseUser;
    }

}