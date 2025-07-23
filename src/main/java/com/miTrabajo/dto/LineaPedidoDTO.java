// src/main/java/com/miTrabajo/dto/LineaPedidoDTO.java
package com.miTrabajo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LineaPedidoDTO {
    private Long id; // Incluye el ID si es una respuesta de un pedido existente
    @NotNull private Long productoId;
    private String nombreProducto; // Información del producto para la respuesta
    private double precioUnitario; // Precio del producto al momento del pedido
    @Min(1) int cantidad;
    private double subtotal; // Subtotal de la línea de pedido

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}