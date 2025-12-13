package com.control.yape.controller;

import com.control.yape.dto.DashboardTotalDTO;
import com.control.yape.dto.YapeMovimientoDTO;
import com.control.yape.dto.YapeNotificacionRequest;
import com.control.yape.dto.YapeNotificacionResponse;
import com.control.yape.model.Usuario;
import com.control.yape.model.YapeMovimiento;
import com.control.yape.security.AuthUsuarioService;
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
    private final AuthUsuarioService authUsuarioService;

    public YapeMovimientoController(YapeMovimientoService movimientoService,
                                     AuthUsuarioService authUsuarioService) {
        this.movimientoService = movimientoService;
        this.authUsuarioService = authUsuarioService;
    }

    // 🔹 LISTAR TODOS - solo de su empresa
    @GetMapping("/yape-movimientos")
    public List<YapeMovimientoDTO> listarTodos() {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        
        if (usuarioActual == null || usuarioActual.getEmpresa() == null) {
            throw new IllegalArgumentException("Usuario no autenticado o sin empresa asignada");
        }
        
        Long empresaId = usuarioActual.getEmpresa().getId();
        
        return movimientoService.listarPorEmpresa(empresaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Movimientos por cuenta (validar que pertenezca a su empresa)
    @GetMapping("/yape-cuentas/{cuentaId}/movimientos")
    public List<YapeMovimientoDTO> listarPorCuenta(@PathVariable Long cuentaId) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaId = usuarioActual.getEmpresa().getId();
        
        movimientoService.validarCuentaPertenece(cuentaId, empresaId);
        
        return movimientoService.listarPorCuenta(cuentaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Registrar movimiento (validar que pertenezca a su empresa)
    @PostMapping("/yape-cuentas/{cuentaId}/movimientos")
    public YapeMovimientoDTO registrar(@PathVariable Long cuentaId,
                                       @RequestBody YapeMovimientoDTO dto) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaId = usuarioActual.getEmpresa().getId();
        
        movimientoService.validarCuentaPertenece(cuentaId, empresaId);
        
        YapeMovimiento mov = toEntity(dto);
        YapeMovimiento creado = movimientoService.registrarMovimiento(cuentaId, mov);
        return toDto(creado);
    }

    // Movimientos por empresa + rango fechas (validar que sea su empresa)
    @GetMapping("/empresas/{empresaId}/movimientos")
    public List<YapeMovimientoDTO> listarPorEmpresaYRango(
            @PathVariable Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        if (!empresaId.equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a esta empresa");
        }
        
        return movimientoService
                .listarPorEmpresaYRangoFechas(empresaId, desde, hasta)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Dashboard: total recibido por empresa (validar que sea su empresa)
    @GetMapping("/empresas/{empresaId}/dashboard/total-recibido")
    public DashboardTotalDTO totalRecibido(
            @PathVariable Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        if (!empresaId.equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a esta empresa");
        }
        
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
    
    // 🔹 Endpoint para la APP Android
    @PostMapping("/yape-movimientos/notify")
    public YapeNotificacionResponse registrarDesdeApp(
            @RequestHeader(name = "X-APP-KEY", required = false) String appKey,
            @RequestBody YapeNotificacionRequest req
    ) {
        final String expectedKey = "SECRETO_CONTROL_YAPE";

        if (appKey == null || !expectedKey.equals(appKey)) {
            YapeNotificacionResponse resp = new YapeNotificacionResponse();
            resp.setStatus("ERROR");
            resp.setDuplicado(false);
            resp.setMensaje("APP_KEY inválido");
            return resp;
        }

        boolean[] duplicadoFlag = new boolean[1];
        YapeMovimiento mov = movimientoService.registrarDesdeApp(req, duplicadoFlag);

        YapeNotificacionResponse resp = new YapeNotificacionResponse();
        resp.setStatus("OK");
        resp.setMovimientoId(mov.getId());
        resp.setDuplicado(duplicadoFlag[0]);
        resp.setMensaje(duplicadoFlag[0]
                ? "Movimiento ya existía (duplicado por firmaUnica)"
                : "Movimiento registrado correctamente");

        return resp;
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
        m.setFechaHora(dto.getFechaHora());
        m.setMensaje(dto.getMensaje());
        m.setEstado(dto.getEstado());
        return m;
    }
}
