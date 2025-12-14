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
                .filter(u -> u.getActivo() != null && u.getActivo()) // Solo usuarios activos
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ============= LISTAR POR EMPRESA (SUPERADMIN ve todas, otros solo su empresa) =============
    @GetMapping("/empresa/{empresaId}")
    public List<UsuarioDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        String rol = usuarioActual.getRol() != null ? usuarioActual.getRol().toUpperCase() : "";
        
        // SUPERADMIN puede ver usuarios de cualquier empresa, otros solo su empresa
        if (!"SUPERADMIN".equals(rol) && !empresaId.equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para ver usuarios de otra empresa");
        }
        
        return usuarioRepository.findByEmpresa_IdOrderByNombreCompleto(empresaId)
                .stream()
                .filter(u -> u.getActivo() != null && u.getActivo()) // Solo usuarios activos
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ============= OBTENER POR ID (validar que sea de su empresa, SUPERADMIN puede obtener cualquiera) =============
    @GetMapping("/{id}")
    public UsuarioDTO obtener(@PathVariable Long id) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        String rol = usuarioActual.getRol() != null ? usuarioActual.getRol().toUpperCase() : "";
        
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + id));
        
        // Validar que el usuario pertenece a su empresa (SUPERADMIN puede obtener cualquiera)
        if (!"SUPERADMIN".equals(rol) && !u.getEmpresa().getId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a este usuario");
        }
        
        return toDTO(u);
    }

    // ============= CREAR (solo en su propia empresa, SUPERADMIN en cualquiera) =============
    @PostMapping
    public UsuarioDTO crear(@RequestBody UsuarioRequest req) {
        // Validaciones para creación (campos requeridos)
        if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username es requerido");
        }
        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password es requerido");
        }
        if (req.getNombreCompleto() == null || req.getNombreCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre completo es requerido");
        }
        if (req.getRol() == null || req.getRol().trim().isEmpty()) {
            throw new IllegalArgumentException("Rol es requerido");
        }
        if (req.getEmpresaId() == null) {
            throw new IllegalArgumentException("Empresa ID es requerido");
        }
        
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        String rol = usuarioActual.getRol() != null ? usuarioActual.getRol().toUpperCase() : "";
        
        // SUPERADMIN puede crear usuarios en cualquier empresa, otros solo en la suya
        if (!"SUPERADMIN".equals(rol) && !req.getEmpresaId().equals(empresaUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para crear usuarios en otra empresa");
        }

        Empresa empresa = empresaRepository.findById(req.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada: " + req.getEmpresaId()));

        Usuario u = new Usuario();
        u.setUsername(req.getUsername());
        u.setPassword(req.getPassword());
        u.setNombreCompleto(req.getNombreCompleto());
        u.setRol(req.getRol());
        u.setEmpresa(empresa);
        u.setActivo(true);

        Usuario creado = usuarioService.crear(u);
        return toDTO(creado);
    }

    // ============= CREAR POR EMPRESA (alternativo para frontend) =============
    @PostMapping("/empresa/{empresaId}")
    public UsuarioDTO crearPorEmpresa(@PathVariable Long empresaId, @RequestBody UsuarioRequest req) {
        req.setEmpresaId(empresaId); // Sobrescribir empresaId desde la URL
        return crear(req);
    }

    // ============= EDITAR (PUT - solo usuarios de su empresa, SUPERADMIN de cualquiera) =============
    @PutMapping("/{id}")
    public UsuarioDTO editar(@PathVariable Long id, @RequestBody UsuarioRequest req) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        String rol = usuarioActual.getRol() != null ? usuarioActual.getRol().toUpperCase() : "";

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + id));
        
        // Validar que pertenece a su empresa (SUPERADMIN puede editar cualquiera)
        if (!"SUPERADMIN".equals(rol) && !u.getEmpresa().getId().equals(empresaUsuario)) {
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

    // ============= ELIMINAR (DELETE) - BORRADO LÓGICO (SUPERADMIN puede eliminar cualquiera) =============
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        Usuario usuarioActual = authUsuarioService.getUsuarioActual();
        Long empresaUsuario = usuarioActual.getEmpresa().getId();
        String rol = usuarioActual.getRol() != null ? usuarioActual.getRol().toUpperCase() : "";
        
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + id));

        // Validar que solo puede eliminar usuarios de su empresa (SUPERADMIN puede eliminar cualquiera)
        if (!"SUPERADMIN".equals(rol) && !u.getEmpresa().getId().equals(empresaUsuario)) {
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
