package com.miTrabajo.repository; // Asegúrate de que este sea el paquete correcto

import com.miTrabajo.model.Role; // Importa tu clase Role
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}