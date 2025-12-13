package com.control.yape.controller;

import com.control.yape.dto.UsuarioDTO;
import com.control.yape.dto.UsuarioRequest;
import com.control.yape.model.Empresa;
import com.control.yape.model.Usuario;
import com.control.yape.repository.EmpresaRepository;
import com.control.yape.repository.UsuarioRepository;
import com.control.yape.security.AuthUsuarioService;
import com.control.yape.service.NotFoundException;
import com.control.yape.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioService usuarioService;
    private final AuthUsuarioService authUsuarioService;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             EmpresaRepository empresaRepository,
                             UsuarioService usuarioService,
                             AuthUsuarioService authUsuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioService = usuarioService;
        this.authUsuarioService = authUsuarioService;
    }

    // ============= LISTAR TODOS (solo de su empresa) =============
    @GetMapping
    public List<UsuarioDTO> listar() {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaId = usuarioActual.getEmpresa().getId();
        
        return usuarioRepository.findByEmpresa_IdOrderByNombreCompleto(empresaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ============= OBTENER POR ID (validar que sea de su empresa) =============
    @GetMapping("/{id}")
    public UsuarioDTO obtener(@PathVariable Long id) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + id));
        
        // Validar que el usuario pertenece a su empresa
        if (!u.getEmpresa().getId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a este usuario");
        }
        
        return toDTO(u);
    }

    // ============= CREAR (solo en su propia empresa) =============
    @PostMapping
    public UsuarioDTO crear(@Valid @RequestBody UsuarioRequest req) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        // Validar que solo puede crear usuarios en su empresa
        if (req.getEmpresaId() == null || !req.getEmpresaId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para crear usuarios en otra empresa");
        }

        Empresa empresa = empresaRepository.findById(req.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada: " + req.getEmpresaId()));

        Usuario u = new Usuario();
        u.setUsername(req.getUsername());
        u.setPassword(req.getPassword()); // se encripta en UsuarioServiceImpl
        u.setNombreCompleto(req.getNombreCompleto());
        u.setRol(req.getRol());
        u.setEmpresa(empresa);
        u.setActivo(true);

        Usuario creado = usuarioService.crear(u);
        return toDTO(creado);
    }

    // ============= EDITAR (PUT - solo usuarios de su empresa) =============
    @PutMapping("/{id}")
    public UsuarioDTO editar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest req) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + id));
        
        // Validar que pertenece a su empresa
        if (!u.getEmpresa().getId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para editar este usuario");
        }

        // empresa (si llega, no puede cambiar de empresa)
        if (req.getEmpresaId() != null && !req.getEmpresaId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No puede cambiar usuarios a otra empresa");
        }

        if (req.getUsername() != null && !req.getUsername().trim().isEmpty()) {
            u.setUsername(req.getUsername().trim());
        }

        if (req.getNombreCompleto() != null && !req.getNombreCompleto().trim().isEmpty()) {
            u.setNombreCompleto(req.getNombreCompleto().trim());
        }

        if (req.getRol() != null && !req.getRol().trim().isEmpty()) {
            u.setRol(req.getRol().trim());
        }

        // password opcional
        if (req.getPassword() != null && !req.getPassword().trim().isEmpty()) {
            u.setPassword(req.getPassword()); // se encripta en UsuarioServiceImpl
        }

        // activo opcional
        if (req.getActivo() != null) {
            u.setActivo(req.getActivo());
        }

        Usuario actualizado = usuarioService.actualizar(u);
        return toDTO(actualizado);
    }

    // ============= ELIMINAR (DELETE) - BORRADO LÓGICO =============
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + id));

        // Validar que solo puede eliminar usuarios de su empresa
        if (!u.getEmpresa().getId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para eliminar este usuario");
        }

        // ✅ Borrado lógico (recomendado)
        u.setActivo(false);
        usuarioService.actualizar(u);
    }

    // ============= MAPPER =============
    private UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setNombreCompleto(u.getNombreCompleto());
        dto.setRol(u.getRol());
        dto.setActivo(u.getActivo());
        if (u.getEmpresa() != null) {
            dto.setEmpresaId(u.getEmpresa().getId());
            dto.setEmpresaNombre(u.getEmpresa().getNombre());
        }
        return dto;
    }
}
