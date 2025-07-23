// src/main/java/com/miTrabajo/config/ProductoMapper.java
package com.miTrabajo.config;

import org.mapstruct.Mapper;
import com.miTrabajo.dto.request.ProductoRequest;
import com.miTrabajo.dto.response.ProductoResponse; // Asumiendo que este es para la respuesta de productos
import com.miTrabajo.model.Producto;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoResponse toDto(Producto producto); // Cambio a toDto para consistencia

    Producto toEntity(ProductoRequest request);
}