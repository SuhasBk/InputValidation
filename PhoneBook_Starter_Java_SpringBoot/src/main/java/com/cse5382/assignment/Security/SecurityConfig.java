package com.cse5382.assignment.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cse5382.assignment.Filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor 
public class SecurityConfig {

    private final CustomAuthProvider authProvider;
    private final JwtFilter jwtFilter;

    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {                
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/api/auth/authenticate").permitAll()
                .requestMatchers("/phoneBook/list").hasAnyAuthority("ROLE_READ", "ROLE_READ_WRITE")
                .requestMatchers("/phoneBook/add", "/phoneBook/deleteByName", "/phoneBook/deleteByNumber").hasAuthority("ROLE_READ_WRITE")
                .anyRequest()
                .authenticated())
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtFilter,  UsernamePasswordAuthenticationFilter.class);
            // .httpBasic(withDefaults());
        return http.build();
    }
    
}
