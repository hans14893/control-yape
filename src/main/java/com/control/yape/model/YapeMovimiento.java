package com.control.yape.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "yape_movimiento",
    indexes = {
        @Index(name = "idx_mov_cuenta_fecha", columnList = "yape_cuenta_id, fecha_hora"),
        @Index(name = "idx_mov_firma_unica", columnList = "firma_unica")
    }
)
public class YapeMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "nombre_cliente", length = 100)
    private String nombreCliente;

    @Column(length = 20)
    private String celular;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(length = 20, nullable = false)
    private String estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "yape_cuenta_id")
    private YapeCuenta yapeCuenta;

    // ✅ firma hash (64 chars) para UNIQUE
    @Column(name = "firma_unica", length = 64, nullable = false, unique = true)
    private String firmaUnica;

    @Column(length = 50)
    private String origen; // ANDROID_APP, MANUAL

    @Column(name = "texto_original", columnDefinition = "TEXT")
    private String textoOriginal;

    @Column(name = "device_id", length = 100)
    private String deviceId;

    @PrePersist
    public void prePersist() {
        if (fechaHora == null) fechaHora = LocalDateTime.now();
        if (estado == null || estado.isBlank()) estado = "RECIBIDO";
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

	public YapeCuenta getYapeCuenta() {
		return yapeCuenta;
	}

	public void setYapeCuenta(YapeCuenta yapeCuenta) {
		this.yapeCuenta = yapeCuenta;
	}

	public String getFirmaUnica() {
		return firmaUnica;
	}

	public void setFirmaUnica(String firmaUnica) {
		this.firmaUnica = firmaUnica;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getTextoOriginal() {
		return textoOriginal;
	}

	public void setTextoOriginal(String textoOriginal) {
		this.textoOriginal = textoOriginal;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

    
    // getters/setters...
}
