package nest.esprit.apigateway.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nest.esprit.apigateway.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthGatewayFilter implements GatewayFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthGatewayFilter.class);

    private final WebClient webClient;

    @Autowired
    public AuthGatewayFilter(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8088").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthGatewayFilter is executing...");

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String path = exchange.getRequest().getPath().value();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/validate")
                        .queryParam("path", path)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .flatMap(userDTO -> {
                    log.info("Received UserDTO from User-Service: {}", userDTO);


                    String userJson = serializeUserDTO(userDTO);


                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-DTO", userJson)
                            .build();

                    log.info("Forwarding Full UserDTO as JSON header: {}", userJson);

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    HttpStatus status = (HttpStatus) ex.getStatusCode();
                    log.error("JWT validation failed: {}", ex.getMessage());

                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(status);
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8");
                    byte[] bytes = ex.getResponseBodyAsString().getBytes(StandardCharsets.UTF_8);

                    return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
                });

    }


    private String serializeUserDTO(UserDTO userDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Add this line
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = objectMapper.writeValueAsString(userDTO);
            log.info("Serialized UserDTO: {}", json);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Error serializing UserDTO", e);
            return "{}";
        }
    }



}