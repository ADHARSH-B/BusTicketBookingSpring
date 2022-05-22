package com.ticketbooking.main.securitycon;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig  implements WebMvcConfigurer {

//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**");
//    }
//	@Bean
//	 public CorsConfigurationSource corsConfigurationSource() {
//	        CorsConfiguration configuration = new CorsConfiguration();
//	        configuration.setAllowedOrigins(Arrays.asList(
//	                "http://localhost:8080",
//	                "http://localhost:4200",
//	                "https://localhost:4200"
//	                )
//	        );
//	        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT", "OPTIONS"));
//	        configuration.setAllowCredentials(true);
//	        configuration.setAllowedHeaders(
//	                Arrays.asList(
//	                        "Access-Control-Allow-Headers",
//	                        "Access-Control-Allow-Origin",
//	                        "Access-Control-Request-Method",
//	                        "Access-Control-Request-Headers",
//	                        "Origin", "Cache-Control",
//	                        "Content-Type",
//	                        "Authorization"));
//	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        source.registerCorsConfiguration("/**", configuration);
//	        return source;
//	    }
}
