package com.control.yape.controller;

import com.control.yape.dto.EmpresaDTO;
import com.control.yape.model.Empresa;
import com.control.yape.model.Usuario;
import com.control.yape.security.AuthUsuarioService;
import com.control.yape.service.EmpresaService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin
public class EmpresaController {

    private final EmpresaService empresaService;
    private final AuthUsuarioService authUsuarioService;

    public EmpresaController(EmpresaService empresaService,
                             AuthUsuarioService authUsuarioService) {
        this.empresaService = empresaService;
        this.authUsuarioService = authUsuarioService;
    }

    // ✅ LISTAR - Solo retorna su empresa, SUPER_ADMIN ve todas
    @GetMapping
    public List<EmpresaDTO> listar() {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        
        if (usuarioActual == null || usuarioActual.getEmpresa() == null) {
            throw new IllegalArgumentException("Usuario no autenticado o sin empresa asignada");
        }
        
        // Si es SUPERADMIN, retorna todas las empresas
        if ("SUPERADMIN".equals(usuarioActual.getRol())) {
            return empresaService.listarActivas()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }
        
        // Si no, solo su empresa
        return List.of(toDto(usuarioActual.getEmpresa()));
    }

    // ✅ OBTENER POR ID - Valida que sea su empresa (SUPER_ADMIN accede a todas)
    @GetMapping("/{id}")
    public EmpresaDTO obtener(@PathVariable Long id) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        // SUPERADMIN puede acceder a cualquier empresa
        if (!"SUPERADMIN".equals(usuarioActual.getRol()) && !id.equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a esta empresa");
        }
        
        Empresa empresa = empresaService.obtenerPorId(id);
        return toDto(empresa);
    }

    // ❌ CREAR - Solo SUPER_ADMIN puede crear empresas
    @PostMapping
    public EmpresaDTO crear(@Valid @RequestBody EmpresaDTO dto) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        
        if (!"SUPERADMIN".equals(usuarioActual.getRol())) {
            throw new IllegalArgumentException("Solo administradores del sistema pueden crear empresas");
        }
        
        Empresa empresa = toEntity(dto);
        Empresa creada = empresaService.crear(empresa);
        return toDto(creada);
    }

    // ✅ ACTUALIZAR - SUPER_ADMIN actualiza cualquier empresa, usuarios su propia empresa
    @PutMapping("/{id}")
    public EmpresaDTO actualizar(@PathVariable Long id,
                                 @Valid @RequestBody EmpresaDTO dto) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        // SUPERADMIN puede actualizar cualquier empresa
        if (!"SUPERADMIN".equals(usuarioActual.getRol()) && !id.equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para actualizar esta empresa");
        }
        
        Empresa data = toEntity(dto);
        Empresa actualizada = empresaService.actualizar(id, data);
        return toDto(actualizada);
    }

    // ❌ DESACTIVAR - Solo SUPER_ADMIN puede desactivar empresas
    @DeleteMapping("/{id}")
    public void desactivar(@PathVariable Long id) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        
        if (!"SUPERADMIN".equals(usuarioActual.getRol())) {
            throw new IllegalArgumentException("Solo administradores del sistema pueden desactivar empresas");
        }
        
        empresaService.desactivar(id);
    }

    // ================== MAPEOS ==================

    private EmpresaDTO toDto(Empresa e) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setRuc(e.getRuc());
        dto.setEmailContacto(e.getEmailContacto());
        dto.setActivo(e.getActivo());
        return dto;
    }

    private Empresa toEntity(EmpresaDTO dto) {
        Empresa e = new Empresa();
        e.setId(dto.getId());
        e.setNombre(dto.getNombre());
        e.setRuc(dto.getRuc());
        e.setEmailContacto(dto.getEmailContacto());
        e.setActivo(dto.getActivo() != null ? dto.getActivo() : Boolean.TRUE);
        return e;
    }
}
