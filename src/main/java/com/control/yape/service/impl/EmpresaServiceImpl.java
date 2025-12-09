package com.control.yape.service.impl;

import com.control.yape.model.Empresa;
import com.control.yape.repository.EmpresaRepository;
import com.control.yape.service.EmpresaService;
import com.control.yape.service.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public List<Empresa> listarActivas() {
        return empresaRepository.findByActivoTrue();
    }

    @Override
    public Empresa obtenerPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada: " + id));
    }

    @Override
    public Empresa crear(Empresa empresa) {
        empresa.setId(null);
        if (empresa.getActivo() == null) empresa.setActivo(true);
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa actualizar(Long id, Empresa data) {
        Empresa existente = obtenerPorId(id);

        existente.setNombre(data.getNombre());
        existente.setRuc(data.getRuc());
        existente.setEmailContacto(data.getEmailContacto());

        if (data.getActivo() != null) {
            existente.setActivo(data.getActivo());
        }

        return empresaRepository.save(existente);
    }

    @Override
    public void desactivar(Long id) {
        Empresa empresa = obtenerPorId(id);
        empresa.setActivo(false);
        empresaRepository.save(empresa);
    }
}
