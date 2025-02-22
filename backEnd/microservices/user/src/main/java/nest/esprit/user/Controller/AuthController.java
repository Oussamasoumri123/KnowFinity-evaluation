package nest.esprit.user.Controller;

import jakarta.servlet.http.HttpServletRequest;
import nest.esprit.user.Configuration.Provider.TokenProvider;
import nest.esprit.user.Configuration.SecurityConfig;
import nest.esprit.user.Entity.DTO.UserDTO;
import nest.esprit.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("path") String path,
            HttpServletRequest request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        String token = authHeader.replace("Bearer ", "");
        String email = tokenProvider.getSubject(token, request);

        if (tokenProvider.isTokenValid(email, token)) {
            UserDTO userDTO = userService.getUserByEmail(email);

            // Dynamic permission check
            String requiredPermission = SecurityConfig.getRequiredPermission(path);

            if (requiredPermission != null && !userDTO.getPermission().contains(requiredPermission)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Missing permission " + requiredPermission);
            }

            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
