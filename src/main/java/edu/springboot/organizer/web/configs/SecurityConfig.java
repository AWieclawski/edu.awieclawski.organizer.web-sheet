package edu.springboot.organizer.web.configs;

import edu.springboot.organizer.web.services.UserSecuredService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <a href="https://www.baeldung.com/spring-security-url-http-authorization">
 * Authorize Request for Certain URL and HTTP Method in Spring Security</a>
 * <p>
 * <a href="https://medium.com/@AlexanderObregon/building-a-login-system-with-spring-boot-and-spring-security-2ef6f110a9cb">
 * Building a Login System with Spring Boot and Spring Security</a>
 * <p>
 * <a href="https://stackoverflow.com/questions/52029258/understanding-requestmatchers-on-spring-security">
 * Understanding requestMatchers() on spring-security</a>
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeRequests()
                .antMatchers("/login", "/error","/register/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true).permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout")).build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserSecuredService userDetailsService, PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
