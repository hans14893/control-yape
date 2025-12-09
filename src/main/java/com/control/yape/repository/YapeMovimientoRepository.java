package com.control.yape.repository;

import com.control.yape.model.YapeMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface YapeMovimientoRepository extends JpaRepository<YapeMovimiento, Long> {

    // Movimientos por cuenta
    List<YapeMovimiento> findByYapeCuenta_IdOrderByFechaHoraDesc(Long cuentaId);

    // Movimientos por empresa en rango de fechas
    @Query("""
           SELECT m
           FROM YapeMovimiento m
           WHERE m.yapeCuenta.empresa.id = :empresaId
             AND m.fechaHora BETWEEN :desde AND :hasta
           ORDER BY m.fechaHora DESC
           """)
    List<YapeMovimiento> findByEmpresaAndRangoFechas(Long empresaId,
                                                     LocalDateTime desde,
                                                     LocalDateTime hasta);

    // Total recibido por empresa en rango (para dashboard)
    @Query("""
           SELECT COALESCE(SUM(m.monto), 0)
           FROM YapeMovimiento m
           WHERE m.yapeCuenta.empresa.id = :empresaId
             AND m.fechaHora BETWEEN :desde AND :hasta
             AND m.estado = 'RECIBIDO'
           """)
    BigDecimal totalRecibidoPorEmpresa(Long empresaId,
                                       LocalDateTime desde,
                                       LocalDateTime hasta);
}
