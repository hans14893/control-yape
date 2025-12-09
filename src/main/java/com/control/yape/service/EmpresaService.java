package com.control.yape.service;

import com.control.yape.model.Empresa;

import java.util.List;

public interface EmpresaService {

    List<Empresa> listarActivas();

    Empresa obtenerPorId(Long id);

    Empresa crear(Empresa empresa);

    Empresa actualizar(Long id, Empresa empresa);

    void desactivar(Long id);
}
