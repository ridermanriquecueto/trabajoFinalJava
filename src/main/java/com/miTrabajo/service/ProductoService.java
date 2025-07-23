// Contenido esperado de E:\trabajoFinalJava\src\main\java\com\miTrabajo\service\ProductoService.java

package com.miTrabajo.service;

import com.miTrabajo.model.Producto;
import com.miTrabajo.repository.ProductoRepository;
import com.miTrabajo.dto.request.ProductoRequest; // ¡Asegúrate de que esta importación esté!
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, ProductoRequest request) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        productoExistente.setNombre(request.getNombre());
        productoExistente.setDescripcion(request.getDescripcion());
        productoExistente.setPrecio(request.getPrecio());
        productoExistente.setCategoria(request.getCategoria());
        productoExistente.setImagenUrl(request.getImagenUrl());
        productoExistente.setStock(request.getStock());

        return productoRepository.save(productoExistente);
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
}