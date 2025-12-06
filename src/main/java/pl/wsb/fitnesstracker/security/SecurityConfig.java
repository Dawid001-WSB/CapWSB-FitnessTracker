package pl.wsb.fitnesstracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfiguracja zabezpieczeń API:
 * - Actuator i H2-console dostępne bez logowania
 * - wszystkie GET-y wymagają zalogowanego użytkownika
 * - pozostałe metody (POST/PUT/DELETE/…) wymagają roli ADMIN
 * - uwierzytelnianie przez Basic Auth, lista użytkowników jest statyczna (in-memory)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // wyłączamy CSRF, żeby łatwo testować POST/PUT/DELETE i H2-console
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Actuator + H2-console bez zabezpieczeń
                        .requestMatchers("/actuator/**", "/h2-console/**").permitAll()
                        // wszystkie GET-y wymagają bycia zalogowanym
                        .requestMatchers(HttpMethod.GET, "/**").authenticated()
                        // pozostałe metody (POST/PUT/DELETE/...) tylko dla ADMINA
                        .anyRequest().hasRole("ADMIN")
                )
                // Basic Auth (login/hasło w nagłówku Authorization)
                .httpBasic(Customizer.withDefaults())
                // pozwalamy na ramki dla H2-console
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();
    }

    /**
     * Encoder haseł – używamy domyślnego DelegatingPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Statyczna lista użytkowników:
     * - user / user123 – rola USER
     * - admin / admin123 – rola ADMIN
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
