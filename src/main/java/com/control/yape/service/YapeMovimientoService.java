package com.control.yape.service;

import com.control.yape.dto.YapeNotificacionRequest;
import com.control.yape.model.YapeMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface YapeMovimientoService {

	List<YapeMovimiento> listarPorCuenta(Long cuentaId);

    List<YapeMovimiento> listarPorEmpresaYRangoFechas(
            Long empresaId,
            LocalDateTime desde,
            LocalDateTime hasta
    );

    YapeMovimiento registrarMovimiento(Long cuentaId, YapeMovimiento movimiento);

    BigDecimal totalRecibidoEmpresa(Long empresaId, LocalDateTime desde, LocalDateTime hasta);
    
    List<YapeMovimiento> listarTodos();
    
    List<YapeMovimiento> listarPorEmpresa(Long empresaId);
    
    YapeMovimiento registrarDesdeApp(YapeNotificacionRequest req, boolean[] duplicadoFlag);

    // ✅ VALIDAR QUE LA CUENTA PERTENECE A UNA EMPRESA
    void validarCuentaPertenece(Long cuentaId, Long empresaId);
}


