// src/main/java/com/miTrabajo/service/PedidoService.java
package com.miTrabajo.service;

import com.miTrabajo.model.Pedido;
import com.miTrabajo.model.LineaPedido;
import com.miTrabajo.model.Producto;
import com.miTrabajo.model.EstadoPedido;
import com.miTrabajo.repository.PedidoRepository;
import com.miTrabajo.repository.ProductoRepository;
import com.miTrabajo.dto.request.PedidoRequest;
import com.miTrabajo.dto.request.LineaPedidoRequest;
import com.miTrabajo.exception.ResourceNotFoundException;
import com.miTrabajo.exception.StockInsuficienteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Pedido crearPedido(PedidoRequest request) {
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido.setEstado(EstadoPedido.PENDIENTE); // O CONFIRMADO, según tu flujo

        for (LineaPedidoRequest lineaRequest : request.getLineas()) {
            Long productId = lineaRequest.getProducto().getId();
            int cantidadSolicitada = lineaRequest.getCantidad();

            Producto producto = productoRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + productId + " no encontrado."));

            if (producto.getStock() < cantidadSolicitada) {
                throw new StockInsuficienteException(
                    "Stock insuficiente para el producto: " + producto.getNombre() +
                    ". Stock disponible: " + producto.getStock() +
                    ", cantidad solicitada: " + cantidadSolicitada);
            }

            LineaPedido linea = new LineaPedido(producto, cantidadSolicitada, producto.getPrecio());
            nuevoPedido.addLinea(linea); // Asocia la línea al pedido y establece la bidireccionalidad

            producto.setStock(producto.getStock() - cantidadSolicitada);
            productoRepository.save(producto);
        }

        return pedidoRepository.save(nuevoPedido);
    }

    @Transactional
    public Pedido actualizarPedido(Long id, PedidoRequest request) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido con ID " + id + " no encontrado."));

        // Opción 1: Revertir stock de líneas antiguas y aplicar nuevas (complejo y propenso a errores)
        // Por simplicidad y robustez, a menudo es mejor solo permitir ciertas actualizaciones
        // o implementar un proceso de "re-ordenar" si los cambios son muy grandes.
        // Aquí implementaremos una actualización que reemplaza las líneas del pedido.
        // Esto implica restaurar stock de líneas viejas y deducir de nuevas.

        // Restaurar stock de líneas existentes
        for (LineaPedido lineaAntigua : pedidoExistente.getLineas()) {
            Producto productoAntiguo = lineaAntigua.getProducto();
            productoAntiguo.setStock(productoAntiguo.getStock() + lineaAntigua.getCantidad());
            productoRepository.save(productoAntiguo);
        }
        pedidoExistente.clearLineas(); // Limpiar las líneas del pedido

        // Añadir nuevas líneas y deducir stock
        for (LineaPedidoRequest lineaRequest : request.getLineas()) {
            Long productId = lineaRequest.getProducto().getId();
            int cantidadSolicitada = lineaRequest.getCantidad();

            Producto producto = productoRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + productId + " no encontrado."));

            if (producto.getStock() < cantidadSolicitada) {
                // Si el stock es insuficiente, debes decidir si revertir toda la transacción
                // o qué mensaje dar. En este caso, lanzará una excepción.
                throw new StockInsuficienteException(
                    "Stock insuficiente para el producto: " + producto.getNombre() +
                    ". Stock disponible: " + producto.getStock() +
                    ", cantidad solicitada: " + cantidadSolicitada);
            }

            LineaPedido nuevaLinea = new LineaPedido(producto, cantidadSolicitada, producto.getPrecio());
            pedidoExistente.addLinea(nuevaLinea);

            producto.setStock(producto.getStock() - cantidadSolicitada);
            productoRepository.save(producto);
        }

        // Puedes actualizar otros campos del pedido si vienen en el request
        // pedidoExistente.setFecha(request.getFecha()); // Si PedidoRequest incluye fecha

        return pedidoRepository.save(pedidoExistente);
    }


    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido con ID " + id + " no encontrado."));
    }

    @Transactional
    public void eliminarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido con ID " + id + " no encontrado."));

        // Opcional: Revertir stock al eliminar el pedido
        for (LineaPedido linea : pedido.getLineas()) {
            Producto producto = linea.getProducto();
            producto.setStock(producto.getStock() + linea.getCantidad());
            productoRepository.save(producto);
        }

        pedidoRepository.delete(pedido);
    }
}