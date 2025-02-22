package nest.esprit.user.Exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import nest.esprit.user.Controller.HttpResponseUser;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
//nzid el errorController implemenetation to avoid el white screen when Exception is up
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
//taaml presque kol meho internal errors
    log.error(exception.getMessage());
     return new ResponseEntity<>(HttpResponseUser.builder()
            .timeStamp(now().toString())
            .reason(exception.getMessage())
             .developerMessage(exception.getMessage())
            .status(HttpStatus.resolve(statusCode.value()))
             .statusCode(statusCode.value())
             .build(),statusCode);

  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

    log.error(exception.getMessage());
    //based on the validation from the depencedency validator
    List<FieldError> fieldErrors=exception.getBindingResult().getFieldErrors();
    String fieldMessage =fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
    return new ResponseEntity<>(HttpResponseUser.builder()
            .timeStamp(now().toString())
            .reason(fieldMessage)
            .developerMessage(exception.getMessage())
            .status(HttpStatus.resolve(statusCode.value()))
            .statusCode(statusCode.value())
            .build(),statusCode);
  }
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<HttpResponseUser> sQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {

    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .reason(exception.getMessage().contains("Duplicate entry") ? "Information already exists" : exception.getMessage())
                    .developerMessage(exception.getMessage())
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .build(), BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<HttpResponseUser> badCredentialsException(BadCredentialsException exception) {

    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .reason(exception.getMessage() + ", Incorrect email or password")
                    .developerMessage(exception.getMessage())
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .build(), BAD_REQUEST);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<HttpResponseUser> apiException(ApiException exception) {

    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .reason(exception.getMessage())
                    .developerMessage(exception.getMessage())
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .build(), BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<HttpResponseUser> accessDeniedException(AccessDeniedException exception) {

    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .reason("Access denied. You don\'t have access")
                    .developerMessage(exception.getMessage())
                    .status(FORBIDDEN)
                    .statusCode(FORBIDDEN.value())
                    .build(), FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<HttpResponseUser> exception(Exception exception) {

    System.out.println(exception);
    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .reason(exception.getMessage() != null ?
                            (exception.getMessage().contains("expected 1, actual 0") ? "Record not found" : exception.getMessage())
                            : "Some error occurred")
                    .developerMessage(exception.getMessage())
                    .status(INTERNAL_SERVER_ERROR)
                    .statusCode(INTERNAL_SERVER_ERROR.value())
                    .build(), INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(JWTDecodeException.class)
  public ResponseEntity<HttpResponseUser> exception(JWTDecodeException exception) {

    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .reason("Could not decode the token")
                    .developerMessage(exception.getMessage())
                    .status(INTERNAL_SERVER_ERROR)
                    .statusCode(INTERNAL_SERVER_ERROR.value())
                    .build(), INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<HttpResponseUser> emptyResultDataAccessException(EmptyResultDataAccessException exception) {

    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .reason(exception.getMessage().contains("expected 1, actual 0") ? "Record not found" : exception.getMessage())
                    .developerMessage(exception.getMessage())
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .build(), BAD_REQUEST);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<HttpResponseUser> disabledException(DisabledException exception) {

    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .developerMessage(exception.getMessage())
                    //.reason(exception.getMessage() + ". Please check your email and verify your account.")
                    .reason("User account is currently disabled")
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value()).build()
            , BAD_REQUEST);
  }

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<HttpResponseUser> lockedException(LockedException exception) {

    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .developerMessage(exception.getMessage())
                    //.reason(exception.getMessage() + ", too many failed attempts.")
                    .reason("User account is currently locked")
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value()).build()
            , BAD_REQUEST);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<HttpResponseUser> dataAccessException(DataAccessException exception) {

    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .reason(processErrorMessage(exception.getMessage()))
                    .developerMessage(processErrorMessage(exception.getMessage()))
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value()).build()
            , BAD_REQUEST);
  }

  private ResponseEntity<HttpResponseUser> createErrorHttpResponse(HttpStatus httpStatus, String reason, Exception exception) {
    return new ResponseEntity<>(
            HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .developerMessage(exception.getMessage())
                    .reason(reason)
                    .status(httpStatus)
                    .statusCode(httpStatus.value()).build()
            , httpStatus);
  }

  private String processErrorMessage(String errorMessage) {
    if(errorMessage != null) {
      if(errorMessage.contains("Duplicate entry") && errorMessage.contains("AccountVerifications")) {
        return "You already verified your account.";
      }
      if(errorMessage.contains("Duplicate entry") && errorMessage.contains("ResetPasswordVerifications")) {
        return "We already sent you an email to reset your password.";
      }
      if(errorMessage.contains("Duplicate entry")) {
        return "Duplicate entry. Please try again.";
      }
    }
    return "Some error occurred";
  }
}
