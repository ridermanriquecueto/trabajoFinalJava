// src/main/java/com/miTrabajo/config/PedidoMapper.java
package com.miTrabajo.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.miTrabajo.dto.PedidoDTO;
import com.miTrabajo.dto.request.PedidoRequest; // Importa PedidoRequest si es necesario para otros mapeos
import com.miTrabajo.model.Pedido;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LineaPedidoMapper.class})
public interface PedidoMapper {

    @Mapping(target = "total", expression = "java(pedido.getTotal())")
    PedidoDTO toDto(Pedido pedido);
    List<PedidoDTO> toDtoList(List<Pedido> pedidos);

    // No se necesita toEntity(PedidoRequest) aquí, el servicio lo construirá
}