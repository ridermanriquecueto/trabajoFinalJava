// src/main/java/com/miTrabajo/config/LineaPedidoMapper.java
package com.miTrabajo.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.miTrabajo.dto.LineaPedidoDTO;
import com.miTrabajo.model.LineaPedido;

@Mapper(componentModel = "spring")
public interface LineaPedidoMapper {

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    @Mapping(target = "subtotal", expression = "java(lineaPedido.getCantidad() * lineaPedido.getPrecioUnitario())")
    LineaPedidoDTO toDto(LineaPedido lineaPedido);

    // No se necesita toEntity(LineaPedidoDTO) aquí, el servicio lo construirá
}