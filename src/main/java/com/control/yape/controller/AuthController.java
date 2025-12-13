package com.control.yape.controller;

import com.control.yape.dto.LoginRequestDTO;
import com.control.yape.dto.LoginResponseDTO;
import com.control.yape.model.Usuario;
import com.control.yape.security.JwtUtil;   // ✅ IMPORTA ESTO
import com.control.yape.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService,
                          PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {

        Usuario usuario = usuarioService.buscarPorUsernameActivo(request.getUsername());

        boolean ok = passwordEncoder.matches(request.getPassword(), usuario.getPassword());
        if (!ok) throw new IllegalArgumentException("Credenciales inválidas");

        // ✅ GENERAR TOKEN
        String token = JwtUtil.generateToken(usuario.getUsername());

        LoginResponseDTO resp = new LoginResponseDTO();
        resp.setToken(token); // ✅ AGREGAR ESTO
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
