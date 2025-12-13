package com.control.yape.service.impl;

import com.control.yape.model.Usuario;
import com.control.yape.repository.UsuarioRepository;
import com.control.yape.service.NotFoundException;
import com.control.yape.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario buscarPorUsernameActivo(String username) {
        return usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() ->
                        new NotFoundException("Usuario no encontrado o inactivo: " + username));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        usuario.setId(null);

        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        // 🔐 encriptar password
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Usuario usuario) {

        if (usuario.getId() == null) {
            throw new IllegalArgumentException("El usuario a actualizar debe tener ID");
        }

        // ⚠️ IMPORTANTE:
        // Solo encriptamos password si viene sin encriptar
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }
}
