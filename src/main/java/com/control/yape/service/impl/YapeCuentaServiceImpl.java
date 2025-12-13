package com.control.yape.service.impl;

import com.control.yape.model.Empresa;
import com.control.yape.model.YapeCuenta;
import com.control.yape.repository.EmpresaRepository;
import com.control.yape.repository.YapeCuentaRepository;
import com.control.yape.service.NotFoundException;
import com.control.yape.service.YapeCuentaService;

import java.util.List;

import org.springframework.stereotype.Service;



@Service
public class YapeCuentaServiceImpl implements YapeCuentaService {

    private final YapeCuentaRepository cuentaRepository;
    private final EmpresaRepository empresaRepository;

    public YapeCuentaServiceImpl(YapeCuentaRepository cuentaRepository,
                                 EmpresaRepository empresaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.empresaRepository = empresaRepository;
    }
    
    @Override
    public List<YapeCuenta> listarPorEmpresa(Long empresaId) {
        return cuentaRepository.findByEmpresa_IdOrderByNombre(empresaId);
    }

   
    @Override
    public YapeCuenta obtenerPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuenta Yape no encontrada: " + id));
    }

    @Override
    public YapeCuenta crear(Long empresaId, YapeCuenta cuenta) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada: " + empresaId));

        // Validar número Yape repetido
        if (cuentaRepository.existsByEmpresa_IdAndNumeroYape(empresaId, cuenta.getNumeroYape())) {
            throw new IllegalArgumentException("Ya existe esa cuenta Yape en la empresa");
        }

        cuenta.setId(null);
        cuenta.setEmpresa(empresa);
        if (cuenta.getActivo() == null) cuenta.setActivo(true);

        return cuentaRepository.save(cuenta);
    }

    @Override
    public YapeCuenta actualizar(Long id, YapeCuenta data) {
        YapeCuenta existente = obtenerPorId(id);

        existente.setNombre(data.getNombre());
        existente.setNumeroYape(data.getNumeroYape());
        existente.setTelefono(data.getTelefono());

        if (data.getActivo() != null) {
            existente.setActivo(data.getActivo());
        }

        return cuentaRepository.save(existente);
    }

    @Override
    public void desactivar(Long id) {
        YapeCuenta cuenta = obtenerPorId(id);
        cuenta.setActivo(false);
        cuentaRepository.save(cuenta);
    }
}
