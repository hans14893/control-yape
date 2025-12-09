package com.control.yape.service;

import com.control.yape.model.YapeCuenta;

import java.util.List;

public interface YapeCuentaService {

    List<YapeCuenta> listarPorEmpresa(Long empresaId);

    YapeCuenta obtenerPorId(Long id);

    YapeCuenta crear(Long empresaId, YapeCuenta cuenta);

    YapeCuenta actualizar(Long id, YapeCuenta cuenta);

    void desactivar(Long id);
}
