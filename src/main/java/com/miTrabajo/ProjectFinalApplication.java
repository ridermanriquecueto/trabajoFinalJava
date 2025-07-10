package com.miTrabajo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean; // Si no usas más @Bean en esta clase, puedes comentar o borrar
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Comentar o borrar
// import org.springframework.boot.CommandLineRunner; // Comentar o borrar

@SpringBootApplication
public class ProjectFinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectFinalApplication.class, args);
    }

    /*
    // --- ESTE BLOQUE ESTÁ COMENTADO Y YA NO SE EJECUTA ---
    @Bean
    public CommandLineRunner passwordEncoderGenerator() {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String rawPassword = "usuario_nuevo";
            String encodedPassword = encoder.encode(rawPassword);
            System.out.println("--------------------------------------------------");
            System.out.println("CONTRASEÑA ENCRIPTADA PARA 'adminpass': " + encodedPassword);
            System.out.println("--------------------------------------------------");
        };
    }
    // ----------------------------------------------------------------------
    */
}