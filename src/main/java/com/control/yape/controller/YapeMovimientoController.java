package com.control.yape.controller;

import com.control.yape.dto.DashboardTotalDTO;
import com.control.yape.dto.YapeMovimientoDTO;
import com.control.yape.model.YapeMovimiento;
import com.control.yape.service.YapeMovimientoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class YapeMovimientoController {

    private final YapeMovimientoService movimientoService;

    public YapeMovimientoController(YapeMovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    // Movimientos por cuenta
    @GetMapping("/yape-cuentas/{cuentaId}/movimientos")
    public List<YapeMovimientoDTO> listarPorCuenta(@PathVariable Long cuentaId) {
        return movimientoService.listarPorCuenta(cuentaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Registrar movimiento en una cuenta
    @PostMapping("/yape-cuentas/{cuentaId}/movimientos")
    public YapeMovimientoDTO registrar(@PathVariable Long cuentaId,
                                       @RequestBody YapeMovimientoDTO dto) {
        YapeMovimiento mov = toEntity(dto);
        YapeMovimiento creado = movimientoService.registrarMovimiento(cuentaId, mov);
        return toDto(creado);
    }

    // Movimientos por empresa + rango fechas
    @GetMapping("/empresas/{empresaId}/movimientos")
    public List<YapeMovimientoDTO> listarPorEmpresaYRango(
            @PathVariable Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return movimientoService
                .listarPorEmpresaYRangoFechas(empresaId, desde, hasta)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Dashboard: total recibido por empresa en rango
    @GetMapping("/empresas/{empresaId}/dashboard/total-recibido")
    public DashboardTotalDTO totalRecibido(
            @PathVariable Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        Double total = movimientoService
                .totalRecibidoEmpresa(empresaId, desde, hasta)
                .doubleValue();

        DashboardTotalDTO dto = new DashboardTotalDTO();
        dto.setEmpresaId(empresaId);
        dto.setDesde(desde);
        dto.setHasta(hasta);
        dto.setTotal(total);

        return dto;
    }

    // ================== MAPEOS ==================

    private YapeMovimientoDTO toDto(YapeMovimiento m) {
        YapeMovimientoDTO dto = new YapeMovimientoDTO();
        dto.setId(m.getId());
        dto.setMonto(m.getMonto());
        dto.setNombreCliente(m.getNombreCliente());
        dto.setCelular(m.getCelular());
        dto.setFechaHora(m.getFechaHora());
        dto.setMensaje(m.getMensaje());
        dto.setEstado(m.getEstado());

        if (m.getYapeCuenta() != null) {
            dto.setYapeCuentaId(m.getYapeCuenta().getId());
            dto.setYapeCuentaNombre(m.getYapeCuenta().getNombre());

            if (m.getYapeCuenta().getEmpresa() != null) {
                dto.setEmpresaId(m.getYapeCuenta().getEmpresa().getId());
                dto.setEmpresaNombre(m.getYapeCuenta().getEmpresa().getNombre());
            }
        }

        return dto;
    }

    private YapeMovimiento toEntity(YapeMovimientoDTO dto) {
        YapeMovimiento m = new YapeMovimiento();
        m.setId(dto.getId());
        m.setMonto(dto.getMonto());
        m.setNombreCliente(dto.getNombreCliente());
        m.setCelular(dto.getCelular());
        m.setFechaHora(dto.getFechaHora()); // si viene null, el service pone ahora()
        m.setMensaje(dto.getMensaje());
        m.setEstado(dto.getEstado());
        // yapeCuenta se setea en el service con el {cuentaId}
        return m;
    }
}
