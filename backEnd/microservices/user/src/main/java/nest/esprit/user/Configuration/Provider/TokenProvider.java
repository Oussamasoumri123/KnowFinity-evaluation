package nest.esprit.user.Configuration.Provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import nest.esprit.user.Configuration.UserPrincipal;
import nest.esprit.user.Service.UserService;

import java.util.Date;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
public class TokenProvider {
    @Autowired
    private UserService userService ;
    private static final String KNOWFINIFTY = "KNOW_FINIFTY";
    private static final String USER_MANAGEMENT_SERVICE =  "USER_MANAGEMENT_SERVICE";
    private static final String AUTHORITIES = "authorities";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 400_000_000;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000 ;//5 days

    @Value("${jwt.secret}")
    private String secret;
public String createAccessToken(UserPrincipal userPrincipal) {
return JWT.create().withIssuer(KNOWFINIFTY).withAudience(USER_MANAGEMENT_SERVICE).withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
        .withArrayClaim(AUTHORITIES,getClaimsFromUser(userPrincipal))
        .withExpiresAt(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(secret.getBytes()));

}

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
    return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
}
public String createRefreshToken(UserPrincipal userPrincipal) {
        return JWT.create().withIssuer(KNOWFINIFTY).withAudience(USER_MANAGEMENT_SERVICE).withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+REFRESH_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));

    }
    public List<GrantedAuthority>getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(toList());
    }

    private String[] getClaimsFromToken(String token) {
    //return JWT.decode(token).getClaim("authorities").asArray(String.class);
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);

}

    private JWTVerifier getJWTVerifier() {
    JWTVerifier verifier;
    try
    {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        verifier=JWT.require(algorithm).withIssuer(KNOWFINIFTY).build();

    }
    catch (JWTVerificationException e){
        throw new JWTVerificationException("token verification failed");
    }
return verifier;
}
public Authentication getAuthentication (String email,List<GrantedAuthority> authorities, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthToken =             new UsernamePasswordAuthenticationToken(userService.getUserByEmail(email),null,authorities);
    usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    return usernamePasswordAuthToken;
}

    public boolean isTokenValid(String email, String token) {
    JWTVerifier verifier = getJWTVerifier();
    return StringUtils.isNotEmpty(email)&& !isTokenExpired(verifier,token);
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
    Date expiration = verifier.verify(token).getExpiresAt();
    return expiration.before(new Date());
}
public String getSubject(String token,HttpServletRequest request) {

    try{
        return getJWTVerifier().verify(token).getSubject();
    }
    catch (TokenExpiredException e){
      request.setAttribute("tokenExpired", e.getMessage());
      throw e;
    }
    catch(InvalidClaimException e ){
      request.setAttribute("invalidClaim", e.getMessage());
      throw e;
    }
    catch (Exception e){
        throw e;
    }
}

}