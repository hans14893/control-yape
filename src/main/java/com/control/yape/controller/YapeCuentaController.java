package com.control.yape.controller;

import com.control.yape.dto.YapeCuentaDTO;
import com.control.yape.model.YapeCuenta;
import com.control.yape.service.YapeCuentaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class YapeCuentaController {

    private final YapeCuentaService cuentaService;

    public YapeCuentaController(YapeCuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    // Listar cuentas de una empresa
    @GetMapping("/empresas/{empresaId}/yape-cuentas")
    public List<YapeCuentaDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        return cuentaService.listarPorEmpresa(empresaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/yape-cuentas/{id}")
    public YapeCuentaDTO obtener(@PathVariable Long id) {
        YapeCuenta cuenta = cuentaService.obtenerPorId(id);
        return toDto(cuenta);
    }

    @PostMapping("/empresas/{empresaId}/yape-cuentas")
    public YapeCuentaDTO crear(@PathVariable Long empresaId,
                               @RequestBody YapeCuentaDTO dto) {
        YapeCuenta cuenta = toEntity(dto);
        YapeCuenta creada = cuentaService.crear(empresaId, cuenta);
        return toDto(creada);
    }

    @PutMapping("/yape-cuentas/{id}")
    public YapeCuentaDTO actualizar(@PathVariable Long id,
                                    @RequestBody YapeCuentaDTO dto) {
        YapeCuenta data = toEntity(dto);
        YapeCuenta actualizada = cuentaService.actualizar(id, data);
        return toDto(actualizada);
    }

    @DeleteMapping("/yape-cuentas/{id}")
    public void desactivar(@PathVariable Long id) {
        cuentaService.desactivar(id);
    }

    // ================== MAPEOS ==================

    private YapeCuentaDTO toDto(YapeCuenta c) {
        YapeCuentaDTO dto = new YapeCuentaDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setNumeroYape(c.getNumeroYape());
        dto.setTelefono(c.getTelefono());
        dto.setActivo(c.getActivo());

        if (c.getEmpresa() != null) {
            dto.setEmpresaId(c.getEmpresa().getId());
            dto.setEmpresaNombre(c.getEmpresa().getNombre());
        }

        return dto;
    }

    private YapeCuenta toEntity(YapeCuentaDTO dto) {
        YapeCuenta c = new YapeCuenta();
        c.setId(dto.getId());
        c.setNombre(dto.getNombre());
        c.setNumeroYape(dto.getNumeroYape());
        c.setTelefono(dto.getTelefono());
        c.setActivo(dto.getActivo() != null ? dto.getActivo() : Boolean.TRUE);
        // La empresa se setea en el service usando empresaId del path
        return c;
    }
}
