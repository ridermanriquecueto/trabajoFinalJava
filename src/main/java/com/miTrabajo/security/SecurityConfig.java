package com.miTrabajo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// Asegúrate de tener esta importación
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Para usar el filtro

import java.util.Arrays;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.filter.CorsFilter; // Importar CorsFilter de Spring Web


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            // --- CAMBIO CLAVE AQUÍ: DESACTIVAR el manejo CORS de Spring Security por defecto ---
            // Y luego inyectar nuestro propio CorsFilter explícitamente si fuera necesario.
            // Para depurar, comentamos el .cors(...) para ver si es el culpable.
            // .cors(corsConfigurer -> {
            //     corsConfigurer.configurationSource(corsConfigurationSource());
            // })
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api/lineas/**",
                    "/api/productos/**",
                    "/api/pedidos/**",
                    "/", "/index.html", "/css/**", "/js/**", "/images/**", "/favicon.ico"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        // --- AÑADIR NUESTRO CorsFilter DE FORMA EXPLICITA, si el problema persiste sin .cors(...) ---
        // Si el problema se resuelve comentando .cors(...), entonces el siguiente bean no es necesario.
        // Pero si sigue el error en Postman, podrías necesitar un @Bean para CorsFilter por separado.
        // Si descomentas este, tendrías que registrarlo como un @Bean separado en esta clase.
        // Por ahora, solo comentamos .cors() en el filterChain.

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
            .username("adminuser")
            .password(passwordEncoder.encode("1234"))
            .roles("USER", "ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    // Este bean de CorsConfigurationSource lo seguimos manteniendo
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    // --- NUEVO BEAN OPCIONAL PARA UN CORSFILTER TOTALMENTE MANUAL ---
    // Solo descomentar este bean si el problema persiste DESPUÉS de comentar .cors(...) en filterChain
    /*
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500")); // Origen de tu frontend
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
    */
}