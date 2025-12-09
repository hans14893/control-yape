package com.control.yape.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "yape_movimiento",
       indexes = {
           @Index(name = "idx_mov_cuenta_fecha", columnList = "yape_cuenta_id, fechaHora")
       })
public class YapeMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(length = 100)
    private String nombreCliente;

    @Column(length = 20)
    private String celular;

    @Column(nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(length = 20, nullable = false)
    private String estado = "RECIBIDO"; // o enum si quieres

    @ManyToOne(optional = false)
    @JoinColumn(name = "yape_cuenta_id")
    private YapeCuenta yapeCuenta;

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

	public YapeCuenta getYapeCuenta() {
		return yapeCuenta;
	}

	public void setYapeCuenta(YapeCuenta yapeCuenta) {
		this.yapeCuenta = yapeCuenta;
	}

    
}
