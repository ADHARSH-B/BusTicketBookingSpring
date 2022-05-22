package com.ticketbooking.main.securitycon;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ticketbooking.main.filter.CustomAuthenticationFilter;
import com.ticketbooking.main.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	PasswordEncoder encoder;

//	@Autowired
//	UserDetailsService myuserdetailsserivce;

	@Autowired
	MyUserDetailsService myuserdetailsserivce;
	@Autowired
	CustomAuthenticationFilter customAuthenticationFilter;

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(myuserdetailsserivce).passwordEncoder(encoder);
//	}
	  @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//	        configuration.setAllowedOrigins(Arrays.asList(
//	                "http://localhost:8080",
//	                "http://localhost:4200",
//	                "https://localhost:4200"
//	              
//	                )
//	        );
	        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT", "OPTIONS"));
	        configuration.setAllowCredentials(true);
	        configuration.setAllowedHeaders(
	                Arrays.asList(
	                        "Access-Control-Allow-Headers",
	                        "Access-Control-Allow-Origin",
	                        "Access-Control-Request-Method",
	                        "Access-Control-Request-Headers",
	                        "Origin", "Cache-Control",
	                        "Content-Type",
	                        "Authorization"));
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(myuserdetailsserivce).passwordEncoder(encoder);
	}

	@Override
	public void configure(HttpSecurity security) throws Exception {
//
//		security.cors().configurationSource(corsConfigurationSource()).and().csrf().disable().authorizeRequests().antMatchers("*/oauth2/*").permitAll().anyRequest().authenticated()
//           .and()
//           .oauth2Login();
		security.cors().configurationSource(corsConfigurationSource()).and().csrf().disable().authorizeRequests().antMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
		.antMatchers("/api/v1/user/searchBuses").permitAll().antMatchers("/api/v1/user/getAllRoutes").permitAll().antMatchers("*/oauth/*").permitAll()
		.antMatchers("/api/v1/user/**").hasAnyAuthority("USER","ADMIN").antMatchers("/api/v1/auth/**").permitAll()
				.anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		
//		security.csrf().disable().authorizeRequests().antMatchers("/api/v1/auth/**").permitAll()
//		.antMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
//		.antMatchers("/api/v1/user/**").hasAuthority("USER").anyRequest().authenticated().
//		and().sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//		.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		security.exceptionHandling().accessDeniedHandler((request, response, e) -> {
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().write(
					new JSONObject().put("timestamp", LocalDateTime.now()).put("message", "Access denied").toString());
		});
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
}

//security.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
//response.setContentType("application/json");
//response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//response.getWriter().write(
//		new JSONObject().put("timestamp", LocalDateTime.now()).put("message", "Access denied").toString());
//});