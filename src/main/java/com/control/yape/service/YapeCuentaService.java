package com.control.yape.service;

import java.util.List;

import com.control.yape.model.YapeCuenta;


public interface YapeCuentaService {

    
	YapeCuenta obtenerPorId(Long id);
    YapeCuenta crear(Long empresaId, YapeCuenta cuenta);
    YapeCuenta actualizar(Long id, YapeCuenta cuenta);
    void desactivar(Long id);
    List<YapeCuenta> listarPorEmpresa(Long empresaId); 
}
