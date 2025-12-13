package com.control.yape.repository;

import com.control.yape.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findByActivoTrue();
    Optional<Empresa> findByNombre(String nombre);

}
