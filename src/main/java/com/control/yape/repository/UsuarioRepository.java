// src/main/java/com/control/yape/repository/UsuarioRepository.java
package com.control.yape.repository;

import com.control.yape.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameAndActivoTrue(String username);

    Optional<Usuario> findByUsername(String username); // 👈 este faltaba

    List<Usuario> findByEmpresa_Id(Long empresaId);
    
    List<Usuario> findByEmpresa_IdOrderByNombreCompleto(Long empresaId);
}
