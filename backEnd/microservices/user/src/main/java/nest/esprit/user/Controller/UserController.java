package nest.esprit.user.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import nest.esprit.user.Configuration.Provider.TokenProvider;
import nest.esprit.user.Configuration.UserPrincipal;
import nest.esprit.user.Entity.DTO.UserDTO;
import nest.esprit.user.Entity.DTO.UserDtoRowmapper;
import nest.esprit.user.Entity.User;
import nest.esprit.user.Exception.ApiException;
import nest.esprit.user.Service.ResetPasswordService;
import nest.esprit.user.Service.RoleService;
import nest.esprit.user.Service.UserService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final TokenProvider tokenProvider;
@Autowired
    private  UserService userService;
@Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserDtoRowmapper userDtoRowmapper;
    @Autowired
    private final HttpServletRequest request;
    @Autowired
    private final HttpServletResponse response;
    @Autowired
    private ResetPasswordService resetPasswordService;

//to login
    @PostMapping("/login")
    public ResponseEntity<HttpResponseUser> login(@RequestBody @Valid LoginForm loginForm) {
        try {
           Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));

            UserDTO user=getAuthenticatedUser(authentication);

            //if using mfa send code otherwise just return response ok with user Data
            return user.getUsingMfa() ? sendVerificationCode(user) : sendResponse(user);
        } catch (BadCredentialsException e) {
            // processError(request, response, e);

            throw new ApiException("Bad credentials");
        }
        catch(DisabledException e){
            throw new ApiException("Account Disabled Verify ur account via mail that has been sent to you");
        }
        catch (Exception e) {

            throw new ApiException("Invalid email/password");
        }
    }
    //bech te5ou authenticateurUser bel dto bch t'exposihom lel user
    private UserDTO getAuthenticatedUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }


    @PostMapping("/register")
    public ResponseEntity<HttpResponseUser> saveUser(@RequestBody @Valid User user) {
        UserDTO userDto = userService.createUser(user);
        System.out.println("UserDTO: " + userDto);
        return ResponseEntity.created(getUri()).body(HttpResponseUser.builder()
                .timeStamp(now().toString())
                .data(of("user", userDto))
                .message(String.format("User account created for user %s", user.getFirstName()))
                .status(CREATED)
                .statusCode(CREATED.value())
                .build());
    }
    @PostMapping("/addTutor")
    public ResponseEntity<HttpResponseUser> saveTutor(@RequestBody @Valid User user) {
        UserDTO userDto = userService.createTutor(user);
        System.out.println("UserDTO: " + userDto);
        return ResponseEntity.created(getUri()).body(HttpResponseUser.builder()
                .timeStamp(now().toString())
                .data(of("user", userDto))
                .message(String.format("Tutor account created for user %s", userDto.getFirstName()))
                .status(CREATED)
                .statusCode(CREATED.value())
                .build());
    }
   /* @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return ResponseEntity.badRequest().body(HttpResponse.builder()
                .timeStamp(now().toString())
                        .reason("there is no mapping for a " + request.getMethod()+"request for this path on the server "+ request.getRequestURI() )
                .status(NOT_FOUND)
                .statusCode(NOT_FOUND.value())
                .build());
    }*/


    //refresh token to return an acces token to the user to save it wadha7 ?
    @GetMapping("/refresh/token")
        public ResponseEntity<HttpResponseUser> refreshToken( HttpServletRequest request) {
        if (isHeaderAndTokenValid(request)){
            String token = request.getHeader("Authorization").substring("Bearer ".length());
            UserDTO user=userService.getUserByEmail(tokenProvider.getSubject(token,request));
            return ResponseEntity.created(getUri()).body(HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .data(Map.of("user", user
                            , "access_token",tokenProvider.createAccessToken(getUserPrincipal(user)),
                            "refresh_token",token))
                    .message("token refreshed")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .build());
        }
        else {
            return ResponseEntity.badRequest().body(HttpResponseUser.builder()
                    .timeStamp(now().toString())
                    .reason("refresh token unvalid or missing ")
                    .developerMessage("refresh token unvalid or missing")
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .build());
        }}

    private boolean isHeaderAndTokenValid(HttpServletRequest request) {
        return request.getHeader("Authorization") !=null
                && request.getHeader("Authorization").startsWith("Bearer ")
                && tokenProvider.isTokenValid(
                        tokenProvider.getSubject(request.getHeader("Authorization")
                .substring("bearer ".length()),request),request.getHeader("Authorization").substring("Bearer ".length()));
    }


    //two factor verification that has been sent via sms
    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponseUser> VerifyCode(@PathVariable String email, @PathVariable String code) {
        UserDTO user= userService.verifyCode(email,code);

        return ResponseEntity.ok()
                .body(
                        HttpResponseUser.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("user", user
                                        , "access_token",tokenProvider.createAccessToken(getUserPrincipal(user)),
                                        "refresh_token",tokenProvider.createRefreshToken(getUserPrincipal(user))))
                                .message("Login Success")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }

    //to verify key for account verification
    @GetMapping("/verify/account/{key}")
    public ResponseEntity<HttpResponseUser> verifyAccount(@PathVariable String key) {
        UserDTO user= userService.verifyAccount(key);

        return ResponseEntity.ok()
                .body(
                        HttpResponseUser.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("user", user))
                                .message("Account verified Success")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }
    //to verify key for password reset
    @GetMapping("/verify/password/{key}")
    public ResponseEntity<HttpResponseUser> VerifyCode(@PathVariable String key) {
        UserDTO user= userService.verifyPassword(key);

        return ResponseEntity.ok()
                .body(
                        HttpResponseUser.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("user", user
                                    /*    , "access_token",tokenProvider.createAccessToken(getUserPrincipal(user)),
                                        "refresh_token",tokenProvider.createRefreshToken(getUserPrincipal(user))*/))
                                .message("verified key for password reset")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }
    //to renew password with key and new password and confirm password
        @PostMapping("/resetpassword/{key}/{password}/{ConfirmPassword}")
    public ResponseEntity<HttpResponseUser> VerifyPasswordUrl(@PathVariable String key, @PathVariable String password, @PathVariable String ConfirmPassword ) {
         userService.renewPassword(key,password,ConfirmPassword);

        return ResponseEntity.ok()
                .body(
                        HttpResponseUser.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .message("Password successfully rested ")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }


    //to send (demand) email to reset password with key
    @GetMapping("/RestPassword/{email}")
    public ResponseEntity<HttpResponseUser> resetPassword(@PathVariable String email) {
       resetPasswordService.resetPassword(email);

        return ResponseEntity.ok()
                .body(
                        HttpResponseUser.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .message("Email sent check your mail to reset your password ")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }

//to return profile DTO
    @GetMapping("/profile")
    public ResponseEntity<HttpResponseUser> getProfile(Authentication authentication) {
        //the name of the authentication howa el email
UserDTO user= userService.getUserByEmail(((UserDTO)authentication.getPrincipal()).getEmail());

        return ResponseEntity.ok()
                .body(
                        HttpResponseUser.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("user", user))
                                .message("profile retrieved ")
                                .developerMessage("profile retrieved via access token")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }
    private URI getUri() {
        return URI.create(fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }



    private ResponseEntity<HttpResponseUser> sendResponse(UserDTO user) {
        return ResponseEntity.ok()
                .body(
                        HttpResponseUser.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("user", user
                                        , "access_token",tokenProvider.createAccessToken(getUserPrincipal(user)),
                                        "refresh_token",tokenProvider.createRefreshToken(getUserPrincipal(user))))
                                .message("Login Success")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }

    private UserPrincipal getUserPrincipal(UserDTO user) {
        User user1 = UserDtoRowmapper.toUser(user);
        return new UserPrincipal(UserDtoRowmapper.toUser(userService.getUserByEmail(user1.getEmail())),roleService.GetRoleByUserEmail(user1.getEmail()) );
    }

//2fa
    private ResponseEntity<HttpResponseUser> sendVerificationCode(UserDTO user) {
        userService.sendVerifCode(user);
        return ResponseEntity.ok()
                .body(
                        HttpResponseUser.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .message("Verification Code Sent")
                                .status(HttpStatus.OK)
                                .data(Map.of("user", user))
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }
}
