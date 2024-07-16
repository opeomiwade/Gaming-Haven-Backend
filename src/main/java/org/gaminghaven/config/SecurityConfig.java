package org.gaminghaven.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;


    /**
     * Allows web based security to be configured for specific http requests
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                cors().
                and().
                authorizeHttpRequests((authorize) -> authorize
                        .antMatchers("/users/login").permitAll()
                        .antMatchers("/users/access-token").permitAll()      // Allow access without authentication
                        .antMatchers("/users/signup").permitAll()             // Allow access without authentication
                        .antMatchers("/users/user/{email}").permitAll()      // Allow access without authentication
                        .antMatchers("/users/**").authenticated()        // Require authentication for all other /users/** routes
                        .antMatchers("/listings/add").authenticated() // Require authentication for the lsitings route
                        .antMatchers("/listings/delete").authenticated() // Require authentication for the lsitings route
                        .antMatchers("users/add-saved-listing").authenticated()
                        .antMatchers("users/remove-saved-listing").authenticated()
                        .anyRequest().permitAll()
                ).
                csrf().disable().
                sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                authenticationProvider(authenticationProvider).addFilterBefore(
                        jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).
                logout().
                logoutUrl("/users/logout");
        return http.build();
    }
}
