package com.miTrabajo.config;

import org.mapstruct.Mapper;

import com.miTrabajo.dto.request.PedidoRequest;
import com.miTrabajo.dto.response.PedidoResponse;
import com.miTrabajo.model.Pedido;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    PedidoResponse toDto(Pedido pedido);

    Pedido toEntity(PedidoRequest request);
}
