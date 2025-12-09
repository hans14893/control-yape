package com.control.yape.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "yape_cuenta",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_empresa_numero_yape",
                columnNames = {"empresa_id", "numero_yape"})
    }
)
public class YapeCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ej: "Caja Principal", "Local 2", etc.
    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(name = "numero_yape", length = 20, nullable = false)
    private String numeroYape;

    @Column(length = 20)
    private String telefono;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    private Boolean activo = true;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNumeroYape() { return numeroYape; }
    public void setNumeroYape(String numeroYape) { this.numeroYape = numeroYape; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
