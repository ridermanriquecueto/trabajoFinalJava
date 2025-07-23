package com.miTrabajo.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.miTrabajo.config.ProductoMapper;
import com.miTrabajo.dto.request.ProductoRequest;
import com.miTrabajo.dto.response.ProductoResponse;
import com.miTrabajo.model.Producto;
import com.miTrabajo.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
// Asegúrate de que no haya @CrossOrigin aquí
public class ProductoController {

    private final ProductoService service;
    private final ProductoMapper mapper;

    public ProductoController(ProductoService service, ProductoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // Endpoint para listar TODOS los productos
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar() {
        List<ProductoResponse> response = service.listar().stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(response);
    }

    // --- ¡NUEVO! Endpoint para obtener un producto por su ID ---
    @GetMapping("/{id}") // Mapea GET requests a /api/productos/{id}
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        Producto producto = service.obtenerPorId(id); // Asume que ProductoService tiene este método
        return ResponseEntity.ok(mapper.toDto(producto));
    }
    // --------------------------------------------------------

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody @Valid ProductoRequest request) {
        Producto producto = mapper.toEntity(request);
        Producto creado = service.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProductoRequest request) {
        Producto actualizado = service.actualizar(id, request);
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}