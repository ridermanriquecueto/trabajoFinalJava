package com.miTrabajo.config;

import org.mapstruct.Mapper;

import com.miTrabajo.dto.LineaPedidoDTO;
import com.miTrabajo.model.LineaPedido;

@Mapper(componentModel = "spring")
public interface LineaPedidoMapper {
    LineaPedidoDTO toDto(LineaPedido lineaPedido);
    LineaPedido toEntity(LineaPedidoDTO lineaPedidoDTO);

}
