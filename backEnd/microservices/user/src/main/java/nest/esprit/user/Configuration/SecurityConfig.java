package nest.esprit.user.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import nest.esprit.user.Configuration.Handler.CustomAccesDeniedHandler;
import nest.esprit.user.Configuration.Handler.CustomAuthenticationEntryPoint;
import nest.esprit.user.Configuration.filter.CustomAuthorizationFilter;


import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Component
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] PUBLIC_URLS = {"/user/login/**","/user/register/**","/user/verify/code/**",
            "/user/RestPassword/**","/user/verify/password/**","/user/resetpassword/**","/user/verify/account/**"
    ,"/user/refresh/token/**","/user/addTutor","/user/addAdmin","/user/adminLogin","/updateProfile"};
    //map de declaration des permissions pour chaque path de microservice
    private static final Map<String, String> PATH_PERMISSION_MAP = createPathPermissionMap();

    private static Map<String, String> createPathPermissionMap() {
        Map<String, String> map = new HashMap<>();
            map.put("/course/test", "ADMIN:ALL"); // Example
        map.put("/course/create", "COURS:WRITE");

        return map;
    }
    public static String getRequiredPermission(String path) {
        return PATH_PERMISSION_MAP.get(path);}

    private final BCryptPasswordEncoder encoder ;
    private final CustomAccesDeniedHandler customAccesDeniedHandler ;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint ;
    private final UserDetailsService userDetailsService ;
    @Autowired
    private CustomAuthorizationFilter customAuthorizationFilter;

    public SecurityConfig(
            BCryptPasswordEncoder encoder,
            CustomAccesDeniedHandler customAccesDeniedHandler,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            UserDetailsService userDetailsService) {
        this.encoder = encoder;
        this.customAccesDeniedHandler = customAccesDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        http.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_URLS).permitAll());
        http.authorizeHttpRequests(request -> request.requestMatchers(OPTIONS).permitAll()); // Not needed
        http.authorizeHttpRequests(request -> request.requestMatchers(DELETE, "/user/delete/**").hasAnyAuthority("ADMIN:ALL"));
        http.authorizeHttpRequests(request -> request.requestMatchers(POST, "/api/**").hasAnyAuthority("ADMIN:ALL"));
        http.exceptionHandling(exception -> exception.accessDeniedHandler(customAccesDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint));
        http.authorizeHttpRequests(request -> request.anyRequest().authenticated());
        http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }
}
