// src/main/java/com/control/yape/dto/UsuarioRequest.java
package com.control.yape.dto;

public class UsuarioRequest {

    // Campos opcionales: pueden ser null en PUT (edición), validados en POST (creación)
    private String username;
    
    private String password;
    
    private String nombreCompleto;
    
    private String rol;
    
    // Opcional: si viene en la URL, no es necesario en el body
    private Long empresaId;
    
    private Boolean activo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
    
    
}
