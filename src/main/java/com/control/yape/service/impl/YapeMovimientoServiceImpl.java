package com.control.yape.service.impl;

import com.control.yape.dto.YapeNotificacionRequest;
import com.control.yape.model.YapeCuenta;
import com.control.yape.model.YapeMovimiento;
import com.control.yape.repository.YapeCuentaRepository;
import com.control.yape.repository.YapeMovimientoRepository;
import com.control.yape.service.NotFoundException;
import com.control.yape.service.YapeMovimientoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

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
    public List<YapeMovimiento> listarPorEmpresa(Long empresaId) {
        return movimientoRepository.findByYapeCuenta_Empresa_IdOrderByFechaHoraDesc(empresaId);
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
    public List<YapeMovimiento> listarTodos() {
        // Si quieres ordenado por fecha desc:
        return movimientoRepository.findAllByOrderByFechaHoraDesc();
        // o simple:
        // return repo.findAll();
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
    
    @Override
    public YapeMovimiento registrarDesdeApp(YapeNotificacionRequest req, boolean[] duplicadoFlag) {

        // 1️⃣ Revisar si viene firmaUnica y ya existe
        if (req.getFirmaUnica() != null && !req.getFirmaUnica().isBlank()) {
            Optional<YapeMovimiento> existente =
                    movimientoRepository.findByFirmaUnica(req.getFirmaUnica());

            if (existente.isPresent()) {
                if (duplicadoFlag != null && duplicadoFlag.length > 0) {
                    duplicadoFlag[0] = true;
                }
                return existente.get(); // devolvemos el mismo registro
            }
        }

        // 2️⃣ Resolver la cuenta Yape por número (por ahora usamos solo numeroYapeDestino)
        if (req.getNumeroYapeDestino() == null || req.getNumeroYapeDestino().isBlank()) {
            throw new NotFoundException("numeroYapeDestino es obligatorio para registrar desde app");
        }

        YapeCuenta cuenta = cuentaRepository.findByNumeroYape(req.getNumeroYapeDestino())
                .orElseThrow(() ->
                        new NotFoundException("No se encontró YapeCuenta con número: " + req.getNumeroYapeDestino())
                );

        // 3️⃣ Crear nuevo movimiento
        YapeMovimiento mov = new YapeMovimiento();
        mov.setId(null);
        mov.setYapeCuenta(cuenta);

        // monto
        if (req.getMonto() == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        mov.setMonto(BigDecimal.valueOf(req.getMonto()));

        mov.setNombreCliente(req.getNombreCliente());
        mov.setCelular(req.getCelularCliente());
        mov.setTextoOriginal(req.getTextoOriginal());
        mov.setFirmaUnica(req.getFirmaUnica());
        mov.setOrigen(req.getOrigen() != null ? req.getOrigen() : "ANDROID_APP");
        mov.setDeviceId(req.getDeviceId());

        // fechaHora
        if (req.getFechaMillis() != null) {
            LocalDateTime fecha = Instant.ofEpochMilli(req.getFechaMillis())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            mov.setFechaHora(fecha);
        } else if (mov.getFechaHora() == null) {
            mov.setFechaHora(LocalDateTime.now());
        }

        // estado
        if (mov.getEstado() == null) {
            mov.setEstado("RECIBIDO");
        }

        // 4️⃣ Guardar
        YapeMovimiento guardado = movimientoRepository.save(mov);

        if (duplicadoFlag != null && duplicadoFlag.length > 0) {
            duplicadoFlag[0] = false; // no era duplicado
        }

        return guardado;
    }

    @Override
    public void validarCuentaPertenece(Long cuentaId, Long empresaId) {
        YapeCuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new NotFoundException("Cuenta Yape no encontrada: " + cuentaId));
        
        if (!cuenta.getEmpresa().getId().equals(empresaId)) {
            throw new IllegalArgumentException("La cuenta no pertenece a esta empresa");
        }
    }
}
