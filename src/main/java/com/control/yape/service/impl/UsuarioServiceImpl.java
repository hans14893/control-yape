package com.control.yape.service.impl;

import com.control.yape.model.Usuario;
import com.control.yape.repository.UsuarioRepository;
import com.control.yape.service.NotFoundException;
import com.control.yape.service.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario buscarPorUsernameActivo(String username) {
        return usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado o inactivo: " + username));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        usuario.setId(null);
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }
        return usuarioRepository.save(usuario);
    }
}
