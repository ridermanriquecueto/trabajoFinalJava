// src/main/java/com/miTrabajo/dto/request/LineaPedidoRequest.java
package com.miTrabajo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LineaPedidoRequest {

    @NotNull(message = "El producto no puede ser nulo")
    private ProductoRequestId producto;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    public ProductoRequestId getProducto() { return producto; }
    public void setProducto(ProductoRequestId producto) { this.producto = producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public static class ProductoRequestId {
        @NotNull(message = "El id del producto no puede ser nulo")
        private Long id;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }
}