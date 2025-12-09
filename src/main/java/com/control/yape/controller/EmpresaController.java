package com.control.yape.controller;

import com.control.yape.dto.EmpresaDTO;
import com.control.yape.model.Empresa;
import com.control.yape.service.EmpresaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    public List<EmpresaDTO> listar() {
        return empresaService.listarActivas()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EmpresaDTO obtener(@PathVariable Long id) {
        Empresa empresa = empresaService.obtenerPorId(id);
        return toDto(empresa);
    }

    @PostMapping
    public EmpresaDTO crear(@RequestBody EmpresaDTO dto) {
        Empresa empresa = toEntity(dto);
        Empresa creada = empresaService.crear(empresa);
        return toDto(creada);
    }

    @PutMapping("/{id}")
    public EmpresaDTO actualizar(@PathVariable Long id,
                                 @RequestBody EmpresaDTO dto) {
        Empresa data = toEntity(dto);
        Empresa actualizada = empresaService.actualizar(id, data);
        return toDto(actualizada);
    }

    @DeleteMapping("/{id}")
    public void desactivar(@PathVariable Long id) {
        empresaService.desactivar(id);
    }

    // ================== MAPEOS ==================

    private EmpresaDTO toDto(Empresa e) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setRuc(e.getRuc());
        dto.setEmailContacto(e.getEmailContacto());
        dto.setActivo(e.getActivo());
        return dto;
    }

    private Empresa toEntity(EmpresaDTO dto) {
        Empresa e = new Empresa();
        e.setId(dto.getId());
        e.setNombre(dto.getNombre());
        e.setRuc(dto.getRuc());
        e.setEmailContacto(dto.getEmailContacto());
        e.setActivo(dto.getActivo() != null ? dto.getActivo() : Boolean.TRUE);
        return e;
    }
}
