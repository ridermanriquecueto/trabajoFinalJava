package com.miTrabajo.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.miTrabajo.config.PedidoMapper;
import com.miTrabajo.dto.request.PedidoRequest;
import com.miTrabajo.dto.response.PedidoResponse;
import com.miTrabajo.model.Pedido;
import com.miTrabajo.service.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService service;
    private final PedidoMapper mapper;

    public PedidoController(PedidoService service, PedidoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar() {
        List<PedidoResponse> response = service.listar().stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@RequestBody @Valid PedidoRequest request) {
        Pedido pedido = mapper.toEntity(request);
        Pedido creado = service.guardar(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid PedidoRequest request) {
        Pedido actualizado = service.actualizar(id, request);
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
