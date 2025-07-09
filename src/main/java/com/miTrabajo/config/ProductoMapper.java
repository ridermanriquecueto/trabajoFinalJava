package com.miTrabajo.config;

import org.mapstruct.Mapper;

import com.miTrabajo.dto.request.ProductoRequest;
import com.miTrabajo.dto.response.ProductoResponse;
import com.miTrabajo.model.Producto;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoResponse toDto(Producto producto);

    Producto toEntity(ProductoRequest request);
}
