// src/main/java/com/miTrabajo/controller/PedidoController.java
package com.miTrabajo.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.miTrabajo.config.PedidoMapper;
import com.miTrabajo.dto.PedidoDTO; // Tu DTO de respuesta
import com.miTrabajo.dto.request.PedidoRequest; // Tu DTO de solicitud
import com.miTrabajo.model.Pedido; // El modelo (entidad)
import com.miTrabajo.service.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen
public class PedidoController {

    private final PedidoService service;
    private final PedidoMapper mapper;

    public PedidoController(PedidoService service, PedidoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listar() {
        List<PedidoDTO> response = service.listarPedidos().stream()
                                            .map(mapper::toDto)
                                            .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPorId(@PathVariable Long id) {
        Pedido pedido = service.obtenerPedidoPorId(id);
        return ResponseEntity.ok(mapper.toDto(pedido));
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> crear(@RequestBody @Valid PedidoRequest request) {
        // El servicio toma el request DTO y maneja la creación de la entidad, stock, etc.
        Pedido creado = service.crearPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid PedidoRequest request) {
        // El servicio toma el request DTO y el ID para actualizar
        Pedido actualizado = service.actualizarPedido(id, request);
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarPedido(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}