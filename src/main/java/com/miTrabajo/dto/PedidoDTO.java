// src/main/java/com/miTrabajo/dto/PedidoDTO.java
package com.miTrabajo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import com.miTrabajo.model.EstadoPedido;

public class PedidoDTO {
    private Long id;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    @NotEmpty List<LineaPedidoDTO> lineas;
    @Min(0) private double total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public List<LineaPedidoDTO> getLineas() { return lineas; }
    public void setLineas(List<LineaPedidoDTO> lineas) { this.lineas = lineas; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}