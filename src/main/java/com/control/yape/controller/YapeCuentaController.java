package com.control.yape.controller;

import com.control.yape.dto.YapeCuentaDTO;
import com.control.yape.model.Usuario;
import com.control.yape.model.YapeCuenta;
import com.control.yape.security.AuthUsuarioService;
import com.control.yape.service.YapeCuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/yape-cuentas")
@CrossOrigin
public class YapeCuentaController {

    private final YapeCuentaService cuentaService;
    private final AuthUsuarioService authUsuarioService;

    public YapeCuentaController(YapeCuentaService cuentaService,
                                 AuthUsuarioService authUsuarioService) {
        this.cuentaService = cuentaService;
        this.authUsuarioService = authUsuarioService;
    }

    // ✅ LISTAR POR EMPRESA (solo su propia empresa, SUPERADMIN ve todas)
    @GetMapping("/empresa/{empresaId}")
    public List<YapeCuentaDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        String rol = usuarioActual.getRol() != null ? usuarioActual.getRol().toUpperCase() : "";
        
        // SUPERADMIN puede ver todas las empresas, otros solo su empresa
        if (!"SUPERADMIN".equals(rol) && !empresaId.equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a esta empresa");
        }
        
        return cuentaService.listarPorEmpresa(empresaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ✅ OBTENER POR ID (validar que pertenece a su empresa)
    @GetMapping("/{id}")
    public YapeCuentaDTO obtener(@PathVariable Long id) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        YapeCuenta cuenta = cuentaService.obtenerPorId(id);
        
        // Validar que la cuenta pertenece a su empresa
        if (!cuenta.getEmpresa().getId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a esta cuenta");
        }
        
        return toDto(cuenta);
    }

    // ✅ CREAR (solo en su propia empresa, SUPERADMIN en cualquiera)
    @PostMapping("/empresa/{empresaId}")
    public ResponseEntity<YapeCuentaDTO> crear(@PathVariable Long empresaId,
                                               @RequestBody YapeCuentaDTO dto) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        String rol = usuarioActual.getRol() != null ? usuarioActual.getRol().toUpperCase() : "";
        
        // SUPERADMIN puede crear en cualquier empresa, otros solo en la suya
        if (!"SUPERADMIN".equals(rol) && !empresaId.equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para crear en esta empresa");
        }
        
        YapeCuenta creada = cuentaService.crear(empresaId, toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(creada));
    }

    // ✅ ACTUALIZAR (solo si pertenece a su empresa)
    @PutMapping("/{id}")
    public YapeCuentaDTO actualizar(@PathVariable Long id,
                                    @RequestBody YapeCuentaDTO dto) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        YapeCuenta existente = cuentaService.obtenerPorId(id);
        
        // Validar que la cuenta pertenece a su empresa
        if (!existente.getEmpresa().getId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para actualizar esta cuenta");
        }
        
        YapeCuenta actualizada = cuentaService.actualizar(id, toEntity(dto));
        return toDto(actualizada);
    }

    // ✅ DESACTIVAR (solo si pertenece a su empresa)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        YapeCuenta cuenta = cuentaService.obtenerPorId(id);
        
        // Validar que la cuenta pertenece a su empresa
        if (!cuenta.getEmpresa().getId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para desactivar esta cuenta");
        }
        
        cuentaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== MAPEOS ==================

    private YapeCuentaDTO toDto(YapeCuenta c) {
        YapeCuentaDTO dto = new YapeCuentaDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setNumeroYape(c.getNumeroYape());
        dto.setTelefono(c.getTelefono());
        dto.setActivo(c.getActivo());

        if (c.getEmpresa() != null) {
            dto.setEmpresaId(c.getEmpresa().getId());
            dto.setEmpresaNombre(c.getEmpresa().getNombre());
        }

        return dto;
    }

    private YapeCuenta toEntity(YapeCuentaDTO dto) {
        YapeCuenta c = new YapeCuenta();
        c.setId(dto.getId());
        c.setNombre(dto.getNombre());
        c.setNumeroYape(dto.getNumeroYape());
        c.setTelefono(dto.getTelefono());
        c.setActivo(dto.getActivo() != null ? dto.getActivo() : Boolean.TRUE);
        return c;
    }
}
