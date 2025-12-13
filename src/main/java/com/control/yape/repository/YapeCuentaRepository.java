package com.control.yape.repository;

import com.control.yape.model.YapeCuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface YapeCuentaRepository extends JpaRepository<YapeCuenta, Long> {

    List<YapeCuenta> findByEmpresa_IdAndActivoTrue(Long empresaId);

    Optional<YapeCuenta> findByIdAndEmpresa_Id(Long id, Long empresaId);

    boolean existsByEmpresa_IdAndNumeroYape(Long empresaId, String numeroYape);
    
    Optional<YapeCuenta> findByNumeroYape(String numeroYape);
    
    List<YapeCuenta> findByEmpresa_IdOrderByIdDesc(Long empresaId);
    List<YapeCuenta> findByEmpresa_IdOrderByNombre(Long empresaId);
}
