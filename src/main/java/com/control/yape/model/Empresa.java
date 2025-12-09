package com.control.yape.model;

import jakarta.persistence.*;

@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String nombre;

    @Column(length = 20)
    private String ruc;

    @Column(length = 100)
    private String emailContacto;

    private Boolean activo = true;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
