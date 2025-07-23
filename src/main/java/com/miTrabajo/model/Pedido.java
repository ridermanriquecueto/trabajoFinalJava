// src/main/java/com/miTrabajo/model/Pedido.java
package com.miTrabajo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true) // mappedBy es crucial para bidireccional
    private List<LineaPedido> lineas = new ArrayList<>();

    // Si tienes usuarios y quieres asociar pedidos a ellos
    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    public double getTotal() {
        return lineas.stream()
            .mapToDouble(LineaPedido::getSubtotal) // Usa el subtotal de LineaPedido
            .sum();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public List<LineaPedido> getLineas() { return lineas; }
    public void setLineas(List<LineaPedido> lineas) {
        this.lineas.clear();
        if (lineas != null) {
            for (LineaPedido linea : lineas) {
                this.addLinea(linea); // Usa el método auxiliar
            }
        }
    }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    // public User getUser() { return user; }
    // public void setUser(User user) { this.user = user; }

    // Método auxiliar para añadir línea y mantener la bidireccionalidad
    public void addLinea(LineaPedido lineaPedido) {
        this.lineas.add(lineaPedido);
        lineaPedido.setPedido(this);
    }

    // Método auxiliar para limpiar las líneas y restaurar stock, útil en actualizaciones
    public void clearLineas() {
        this.lineas.clear();
    }
}