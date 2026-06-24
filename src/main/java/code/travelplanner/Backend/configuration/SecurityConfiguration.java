package code.travelplanner.Backend.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {

    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityfIlterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // Anybody can see /register, /login pages as well as load any styling files
                .requestMatchers("/travelplanner/register", "/travelplanner/login", "/css/**").permitAll()
                // Any other URL is blocked unless user has logged in successfully
                .anyRequest().authenticated());

        http.formLogin(form -> form
                // Unauthenticated users redirected to my custom /login page, not Spring security's version
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .permitAll());

        http.logout(logout -> logout
                // When user logs out, user directed back to /login page
                .logoutSuccessUrl("/login?logout")
                .permitAll());

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // TEMPORARY FIX - Localhost only, delete when deployment - security issue
        // Allows for cross-site attacks (not an issue for localhost) makes development easier
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // Allow backend to talk to front end
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:63342"));
        // Allow certain actions for the frontend
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        // Tell browser it is safe to pass session cookies between these ports
        corsConfiguration.setAllowCredentials(true);

        // Allow these CORS rules to every single URL path in the system
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
