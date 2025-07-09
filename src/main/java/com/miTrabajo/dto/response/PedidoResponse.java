package com.miTrabajo.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.miTrabajo.dto.LineaPedidoDTO;
import com.miTrabajo.model.EstadoPedido;
import com.miTrabajo.model.Pedido;

public class PedidoResponse {
    private Long id;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private List<LineaPedidoDTO> lineas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<LineaPedidoDTO> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedidoDTO> lineas) {
        this.lineas = lineas;
    }
}

