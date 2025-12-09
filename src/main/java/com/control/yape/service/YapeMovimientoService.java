package com.control.yape.service;

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
}
