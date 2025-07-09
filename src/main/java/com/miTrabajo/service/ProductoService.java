package com.miTrabajo.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.miTrabajo.dto.request.ProductoRequest;
import com.miTrabajo.exception.ResourceNotFoundException;
import com.miTrabajo.model.Producto;
import com.miTrabajo.repository.ProductoRepository;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Producto buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    public Producto guardar(Producto producto) {
        return repo.save(producto);
    }

    public Producto actualizar(Long id, Producto nuevo) {
        if (nuevo.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (nuevo.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        Producto p = buscarPorId(id);
        p.setNombre(nuevo.getNombre());
        p.setDescripcion(nuevo.getDescripcion());
        p.setPrecio(nuevo.getPrecio());
        p.setCategoria(nuevo.getCategoria());
        p.setImagenUrl(nuevo.getImagenUrl());
        p.setStock(nuevo.getStock());
        return repo.save(p);
    }


    @Transactional
    public Producto actualizar(Long id, ProductoRequest request) {
        Producto existente = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        existente.setNombre(request.getNombre());
        existente.setDescripcion(request.getDescripcion());
        existente.setPrecio(request.getPrecio());
        existente.setStock(request.getStock());
        existente.setCategoria(request.getCategoria());
        existente.setImagenUrl(request.getImagenUrl());

        return repo.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        repo.delete(producto);
    }

}
