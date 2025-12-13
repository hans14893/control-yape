package com.control.yape.dto;

public class YapeNotificacionRequest {

    private Double monto;
    private String moneda;          // "PEN"
    private String nombreCliente;
    private String celularCliente;  // si la app lo puede obtener
    private String textoOriginal;   // notificación cruda
    private Long fechaMillis;       // timestamp desde el celu

    private String firmaUnica;      // hash único
    private String origen;          // "ANDROID_APP"
    private String numeroYapeDestino; // número de la cuenta yape (celular)
    private String deviceId;        // "celular-caja-1"

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCelularCliente() {
        return celularCliente;
    }

    public void setCelularCliente(String celularCliente) {
        this.celularCliente = celularCliente;
    }

    public String getTextoOriginal() {
        return textoOriginal;
    }

    public void setTextoOriginal(String textoOriginal) {
        this.textoOriginal = textoOriginal;
    }

    public Long getFechaMillis() {
        return fechaMillis;
    }

    public void setFechaMillis(Long fechaMillis) {
        this.fechaMillis = fechaMillis;
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

    public String getNumeroYapeDestino() {
        return numeroYapeDestino;
    }

    public void setNumeroYapeDestino(String numeroYapeDestino) {
        this.numeroYapeDestino = numeroYapeDestino;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
