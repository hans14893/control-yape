package com.control.yape.service.impl;

import com.control.yape.model.YapeCuenta;
import com.control.yape.model.YapeMovimiento;
import com.control.yape.repository.YapeCuentaRepository;
import com.control.yape.repository.YapeMovimientoRepository;
import com.control.yape.service.NotFoundException;
import com.control.yape.service.YapeMovimientoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YapeMovimientoServiceImpl implements YapeMovimientoService {

    private final YapeMovimientoRepository movimientoRepository;
    private final YapeCuentaRepository cuentaRepository;

    public YapeMovimientoServiceImpl(YapeMovimientoRepository movimientoRepository,
                                     YapeCuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public List<YapeMovimiento> listarPorCuenta(Long cuentaId) {
        return movimientoRepository.findByYapeCuenta_IdOrderByFechaHoraDesc(cuentaId);
    }

    @Override
    public List<YapeMovimiento> listarPorEmpresaYRangoFechas(
            Long empresaId, LocalDateTime desde, LocalDateTime hasta) {

        return movimientoRepository.findByEmpresaAndRangoFechas(
                empresaId,
                desde,
                hasta
        );
    }

    @Override
    public YapeMovimiento registrarMovimiento(Long cuentaId, YapeMovimiento movimiento) {

        YapeCuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new NotFoundException("Cuenta Yape no encontrada: " + cuentaId));

        movimiento.setId(null);
        movimiento.setYapeCuenta(cuenta);

        if (movimiento.getFechaHora() == null) {
            movimiento.setFechaHora(LocalDateTime.now());
        }
        if (movimiento.getEstado() == null) {
            movimiento.setEstado("RECIBIDO");
        }

        return movimientoRepository.save(movimiento);
    }

    @Override
    public BigDecimal totalRecibidoEmpresa(Long empresaId, LocalDateTime desde, LocalDateTime hasta) {
        return movimientoRepository.totalRecibidoPorEmpresa(empresaId, desde, hasta);
    }
}
