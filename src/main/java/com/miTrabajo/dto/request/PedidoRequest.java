package com.miTrabajo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class PedidoRequest {

    @NotEmpty(message = "Debe tener al menos una línea")
    private List<LineaPedidoRequest> lineas;

    public List<LineaPedidoRequest> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedidoRequest> lineas) {
        this.lineas = lineas;
    }
}

