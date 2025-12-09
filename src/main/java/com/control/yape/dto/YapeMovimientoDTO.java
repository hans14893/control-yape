package com.control.yape.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class YapeMovimientoDTO {

    private Long id;
    private BigDecimal monto;
    private String nombreCliente;
    private String celular;
    private LocalDateTime fechaHora;
    private String mensaje;
    private String estado;

    private Long yapeCuentaId;
    private String yapeCuentaNombre;

    private Long empresaId;
    private String empresaNombre;

    public YapeMovimientoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getYapeCuentaId() {
        return yapeCuentaId;
    }

    public void setYapeCuentaId(Long yapeCuentaId) {
        this.yapeCuentaId = yapeCuentaId;
    }

    public String getYapeCuentaNombre() {
        return yapeCuentaNombre;
    }

    public void setYapeCuentaNombre(String yapeCuentaNombre) {
        this.yapeCuentaNombre = yapeCuentaNombre;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getEmpresaNombre() {
        return empresaNombre;
    }

    public void setEmpresaNombre(String empresaNombre) {
        this.empresaNombre = empresaNombre;
    }
}
