package com.miTrabajo.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.miTrabajo.dto.request.PedidoRequest;
import com.miTrabajo.exception.ResourceNotFoundException;
import com.miTrabajo.exception.StockInsuficienteException;
import com.miTrabajo.model.EstadoPedido;
import com.miTrabajo.model.LineaPedido;
import com.miTrabajo.model.Pedido;
import com.miTrabajo.model.Producto;
import com.miTrabajo.repository.PedidoRepository;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProductoService productoService; // ¡Correcto, ProductoService inyectado!

    public PedidoService(PedidoRepository pedidoRepo, ProductoService productoService) {
        this.pedidoRepo = pedidoRepo;
        this.productoService = productoService;
    }

    @Transactional()
    public List<Pedido> listar() {
        return pedidoRepo.findAllWithLineas();
    }


    @Transactional
    public Pedido guardar(Pedido pedido) {
        if (pedido.getLineas() == null || pedido.getLineas().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos una línea");
        }

        for (LineaPedido linea : pedido.getLineas()) {
            // --- CORRECCIÓN AQUÍ: Cambiar buscarPorId por obtenerPorId ---
            // Antes: Producto producto = productoService.buscarPorId(linea.getProducto().getId());
            // Ahora:
            Producto producto = productoService.obtenerPorId(linea.getProducto().getId());

            // Asociar la instancia gestionada a la línea (esto ya lo estabas haciendo bien)
            linea.setProducto(producto);

            // Verificar stock
            if (producto.getStock() < linea.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre());
            }
        }
        pedido.setEstado(EstadoPedido.PENDIENTE); // Esto está bien
        return pedidoRepo.save(pedido);
    }

    @Transactional
    public Pedido actualizar(Long id, PedidoRequest request) {
        Pedido existente = pedidoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        existente.getLineas().clear(); // Esto está bien

        List<LineaPedido> nuevasLineas = request.getLineas().stream()
                .map(lineaReq -> {
                    // --- CORRECCIÓN AQUÍ: Cambiar buscarPorId por obtenerPorId ---
                    // Antes: Producto producto = productoService.buscarPorId(lineaReq.getProducto().getId());
                    // Ahora:
                    Producto producto = productoService.obtenerPorId(lineaReq.getProducto().getId());
                    LineaPedido linea = new LineaPedido();
                    linea.setProducto(producto);
                    linea.setCantidad(lineaReq.getCantidad());
                    linea.setPedido(existente);
                    return linea;
                }).toList();

        existente.setLineas(nuevasLineas);
        existente.setEstado(EstadoPedido.PENDIENTE); // o el estado actual

        return pedidoRepo.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Pedido pedido = pedidoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
        pedidoRepo.delete(pedido);
    }
}