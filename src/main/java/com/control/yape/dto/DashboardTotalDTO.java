package com.control.yape.dto;

import java.time.LocalDateTime;

public class DashboardTotalDTO {

    private Long empresaId;
    private LocalDateTime desde;
    private LocalDateTime hasta;
    private Double total;

    public DashboardTotalDTO() {
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public LocalDateTime getDesde() {
        return desde;
    }

    public void setDesde(LocalDateTime desde) {
        this.desde = desde;
    }

    public LocalDateTime getHasta() {
        return hasta;
    }

    public void setHasta(LocalDateTime hasta) {
        this.hasta = hasta;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
