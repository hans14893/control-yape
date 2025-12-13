package com.control.yape.service;

import com.control.yape.model.Usuario;

public interface UsuarioService {

    Usuario buscarPorUsernameActivo(String username);

    Usuario crear(Usuario usuario);
    Usuario actualizar(Usuario usuario);
}
