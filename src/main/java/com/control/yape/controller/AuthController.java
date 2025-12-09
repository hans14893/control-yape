package com.control.yape.controller;

import com.control.yape.dto.LoginRequestDTO;
import com.control.yape.dto.LoginResponseDTO;
import com.control.yape.model.Usuario;
import com.control.yape.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {

        // 1. Buscar usuario (activo)
        Usuario usuario = usuarioService.buscarPorUsernameActivo(request.getUsername());

        // 2. Comparar password (por ahora plano, luego ponemos hash/BCrypt)
        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 3. Armar respuesta
        LoginResponseDTO resp = new LoginResponseDTO();
        resp.setUsuarioId(usuario.getId());
        resp.setUsername(usuario.getUsername());
        resp.setNombreCompleto(usuario.getNombreCompleto());
        resp.setRol(usuario.getRol());

        if (usuario.getEmpresa() != null) {
            resp.setEmpresaId(usuario.getEmpresa().getId());
            resp.setEmpresaNombre(usuario.getEmpresa().getNombre());
        }

        return resp;
    }
}
