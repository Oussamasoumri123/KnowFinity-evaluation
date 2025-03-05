package nest.esprit.apigateway;

import nest.esprit.apigateway.filter.AuthGatewayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder, AuthGatewayFilter authGatewayFilter) {
		return builder.routes()
				.route("user-service", r -> r.path("/user/**")
						.uri("lb://USER-SERVICE"))
				.route("course-service", r -> r.path("/course/**")
						.filters(f -> f.filter(authGatewayFilter))
						.uri("lb://COURSE-SERVICE"))


				.build();
	}



}