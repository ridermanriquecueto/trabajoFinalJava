package com.miTrabajo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Asegúrate de que esta línea NO esté comentada
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Asegúrate de que esta línea NO esté comentada
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Asegúrate de que esta línea NO esté comentada

@SpringBootApplication
public class ProjectFinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectFinalApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // Aquí es VITAL que incluyas el origen desde donde abres tu HTML.
                        // Si lo abres directamente, "null" puede funcionar para algunos navegadores.
                        // Si usas Live Server de VS Code, es http://127.0.0.1:5500 o http://localhost:5500.
                        .allowedOrigins("null", "http://localhost:3000", "http://127.0.0.1:5500", "http://localhost:5500") 
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}