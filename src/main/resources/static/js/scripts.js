const API_BASE_URL = 'http://localhost:8080/api';

// --- Funciones de Carga y Visualización de Productos ---
function cargarProductos() {
    fetch(`${API_BASE_URL}/productos`)
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    const errorMessage = errorData.message || `Error ${response.status}: ${response.statusText}`;
                    throw new Error(errorMessage);
                });
            }
            return response.json();
        })
        .then(productos => {
            const contenedor = document.getElementById('productos-container');
            if (!contenedor) {
                console.error("El elemento 'productos-container' no se encontró en el DOM.");
                return;
            }
            contenedor.innerHTML = ''; // Limpiar contenido previo

            if (productos.length === 0) {
                contenedor.innerHTML = '<p>No hay productos disponibles para mostrar.</p>';
                return;
            }

            productos.forEach(p => {
                const div = document.createElement('div');
                div.classList.add('producto');
                div.innerHTML = `
                    <h3>${p.nombre} (ID: ${p.id})</h3>
                    <p>${p.descripcion || 'Sin descripción'}</p>
                    <p><strong>Precio:</strong> $${p.precio ? p.precio.toFixed(2) : 'N/A'}</p>
                    <p><strong>Stock:</strong> ${p.stock !== undefined ? p.stock : 'N/A'}</p>
                    ${p.imagenUrl ? `<img src="${p.imagenUrl}" alt="${p.nombre}" />` : ''}
                    <div class="product-actions">
                        <button class="btn btn-primary btn-agregar-carrito"
                                data-id="${p.id}"
                                data-nombre="${p.nombre}"
                                data-precio="${p.precio}">
                            Añadir al Carrito
                        </button>
                        <button class="btn btn-secondary btn-editar" data-id="${p.id}">Editar</button>
                        <button class="btn btn-cancel btn-eliminar" data-id="${p.id}">Eliminar</button>
                    </div>
                `;
                contenedor.appendChild(div);
            });
            attachProductActionListeners(); // Volver a adjuntar listeners para los nuevos botones
        })
        .catch(err => {
            const contenedor = document.getElementById('productos-container');
            if (contenedor) {
                contenedor.innerHTML = `<p style="color: var(--accent-color);">Error al cargar los productos: ${err.message}.</p>`;
            }
            console.error(`Error en cargarProductos: ${err.message}`, err);
            alert(`No se pudo obtener la lista de productos: ${err.message}. Revisa la consola para más detalles.`);
        });
}

// --- Adjunta listeners a los botones de acción de productos (agregar, editar, eliminar) ---
function attachProductActionListeners() {
    document.querySelectorAll('.btn-agregar-carrito').forEach(button => {
        button.addEventListener('click', (event) => {
            const productId = parseInt(event.target.dataset.id, 10);
            const productName = event.target.dataset.nombre;
            const price = parseFloat(event.target.dataset.precio);
            agregarAlCarrito(productId, productName, price);
        });
    });

    document.querySelectorAll('.btn-editar').forEach(button => {
        button.addEventListener('click', (event) => {
            const productId = parseInt(event.target.dataset.id, 10);
            obtenerProductoParaEditar(productId);
        });
    });

    document.querySelectorAll('.btn-eliminar').forEach(button => {
        button.addEventListener('click', (event) => {
            const productId = parseInt(event.target.dataset.id, 10);
            if (confirm(`¿Estás seguro de que quieres eliminar el producto con ID: ${productId}? Esta acción no se puede deshacer.`)) {
                eliminarProducto(productId);
            }
        });
    });
}

// --- Funciones CRUD de Productos (Agregar, Editar, Eliminar) ---

// Función para agregar un nuevo producto
function agregarProducto(event) {
    event.preventDefault(); // Prevenir el envío por defecto del formulario

    const form = document.getElementById('form-agregar-producto');
    if (!form) return;

    const nuevoProducto = {
        nombre: form.nombre.value,
        descripcion: form.descripcion.value,
        precio: parseFloat(form.precio.value),
        categoria: form.categoria.value,
        imagenUrl: form.imagenUrl.value,
        stock: parseInt(form.stock.value, 10)
    };

    fetch(`${API_BASE_URL}/productos`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(nuevoProducto)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                const errorMessage = errorData.message || `Error ${response.status}: ${response.statusText}`;
                throw new Error(errorMessage);
            });
        }
        return response.json();
    })
    .then(data => {
        alert('Producto agregado correctamente!');
        form.reset(); // Limpiar formulario
        cargarProductos(); // Recargar la lista de productos
    })
    .catch(error => {
        console.error('Error al agregar producto:', error);
        alert(`Error al agregar producto: ${error.message}`);
    });
}

// Función para eliminar un producto
function eliminarProducto(productId) {
    fetch(`${API_BASE_URL}/productos/${productId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                const errorMessage = errorData.message || `Error ${response.status}: ${response.statusText}`;
                throw new Error(errorMessage);
            });
        }
        alert('Producto eliminado correctamente!');
        cargarProductos(); // Recargar la lista
    })
    .catch(error => {
        console.error('Error al eliminar producto:', error);
        alert(`Error al eliminar producto: ${error.message}`);
    });
}

// Función para obtener datos de un producto para edición
// ESTA FUNCIÓN ESTABA CAUSANDO EL PROBLEMA DEL GET, YA QUE NO TENÍAS EL ENDPOINT EN EL BACKEND
// Ahora que lo añadimos con @GetMapping("/{id}"), esto funcionará.
function obtenerProductoParaEditar(productId) {
    fetch(`${API_BASE_URL}/productos/${productId}`)
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    const errorMessage = errorData.message || `Error ${response.status}: ${response.statusText}`;
                    throw new Error(errorMessage);
                });
            }
            return response.json();
        })
        .then(producto => {
            // Rellenar el formulario del modal de edición
            document.getElementById('edit-productId').value = producto.id;
            document.getElementById('edit-nombre').value = producto.nombre;
            document.getElementById('edit-descripcion').value = producto.descripcion;
            document.getElementById('edit-precio').value = producto.precio;
            document.getElementById('edit-categoria').value = producto.categoria;
            document.getElementById('edit-imagenUrl').value = producto.imagenUrl;
            document.getElementById('edit-stock').value = producto.stock;

            // Mostrar el modal
            const modal = document.getElementById('modal-editar-producto');
            if (modal) modal.style.display = 'block';
        })
        .catch(error => {
            console.error('Error al cargar producto para edición:', error);
            alert(`No se pudo cargar el producto para edición: ${error.message}`);
        });
}

// Función para actualizar un producto existente
function actualizarProducto(event) {
    event.preventDefault();

    const form = document.getElementById('form-editar-producto');
    if (!form) return;

    const productId = parseInt(form.elements['edit-productId'].value, 10);

    const productoActualizado = {
        // No incluyas el 'id' si tu backend lo toma del @PathVariable
        // id: productId, 
        nombre: form.elements['edit-nombre'].value,
        descripcion: form.elements['edit-descripcion'].value,
        precio: parseFloat(form.elements['edit-precio'].value),
        categoria: form.elements['edit-categoria'].value,
        imagenUrl: form.elements['edit-imagenUrl'].value,
        stock: parseInt(form.elements['edit-stock'].value, 10)
    };

    fetch(`${API_BASE_URL}/productos/${productId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(productoActualizado)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                const errorMessage = errorData.message || `Error ${response.status}: ${response.statusText}`;
                throw new Error(errorMessage);
            });
        }
        return response.json();
    })
    .then(data => {
        alert('Producto actualizado correctamente!');
        const modal = document.getElementById('modal-editar-producto');
        if (modal) modal.style.display = 'none'; // Ocultar modal
        cargarProductos(); // Recargar la lista de productos
    })
    .catch(error => {
        console.error('Error al actualizar producto:', error);
        alert(`Error al actualizar producto: ${error.message}`);
    });
}

// --- Lógica del carrito de compras ---
let carrito = [];

function agregarAlCarrito(productId, productName, price) {
    const cantidadStr = prompt(`¿Cuántas unidades de "${productName}" (ID: ${productId}) deseas añadir al carrito?`);
    const cantidad = parseInt(cantidadStr, 10);

    if (isNaN(cantidad) || cantidad <= 0) {
        alert('Por favor, ingresa una cantidad válida y positiva.');
        return;
    }

    const itemExistente = carrito.find(item => item.productoId === productId);

    if (itemExistente) {
        itemExistente.cantidad += cantidad;
    } else {
        carrito.push({
            productoId: productId,
            nombre: productName,
            precioUnitario: price,
            cantidad: cantidad
        });
    }
    actualizarVistaCarrito();
}

function actualizarVistaCarrito() {
    const carritoItemsContainer = document.getElementById('carrito-items');
    const carritoTotalSpan = document.getElementById('carrito-total');

    if (!carritoItemsContainer || !carritoTotalSpan) {
        console.error("Elementos del carrito no encontrados.");
        return;
    }

    carritoItemsContainer.innerHTML = '';
    let total = 0;

    if (carrito.length === 0) {
        carritoItemsContainer.innerHTML = '<p>El carrito está vacío.</p>';
        carritoTotalSpan.textContent = '0.00';
        return;
    }

    carrito.forEach(item => {
        const li = document.createElement('li');
        const subtotal = item.cantidad * item.precioUnitario;
        li.innerHTML = `<span>${item.nombre} x ${item.cantidad} ($${item.precioUnitario.toFixed(2)} c/u)</span> <span>Subtotal: $${subtotal.toFixed(2)}</span>`;
        carritoItemsContainer.appendChild(li);
        total += subtotal;
    });

    carritoTotalSpan.textContent = total.toFixed(2);
}

// --- Funciones de Pedidos (Crear y Cargar Historial) ---

// Función para crear un pedido
function crearPedido() {
    if (carrito.length === 0) {
        alert('El carrito está vacío. Agrega productos antes de realizar un pedido.');
        return;
    }

    const userId = 1; // Asumiendo un userId fijo por ahora, puedes cambiarlo según tu lógica de usuario

    const lineaPedidosDTO = carrito.map(item => ({
        productoId: item.productoId,
        cantidad: item.cantidad
    }));

    const pedidoRequest = {
        userId: userId,
        lineaPedidos: lineaPedidosDTO
    };

    fetch(`${API_BASE_URL}/pedidos`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(pedidoRequest)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                const errorMessage = errorData.message || `Error ${response.status}: ${response.statusText}`;
                throw new Error(errorMessage);
            });
        }
        return response.json();
    })
    .then(data => {
        alert(`¡Pedido realizado correctamente! ID del pedido: ${data.id}. Revisa el historial.`);
        carrito = []; // Vaciar carrito
        actualizarVistaCarrito(); // Actualizar vista del carrito
        cargarProductos(); // Recargar productos (por si cambió el stock)
    })
    .catch(error => {
        console.error('Error al crear pedido:', error);
        alert(`Error al crear pedido: ${error.message}. Asegúrate de tener stock suficiente y el carrito con productos.`);
    });
}

// Función para cargar el historial de pedidos
function cargarHistorialPedidos() {
    const userId = 1; // Asumiendo un userId fijo
    const historialContainer = document.getElementById('historial-pedidos-container');

    if (!historialContainer) {
        console.error("El elemento 'historial-pedidos-container' no se encontró en el DOM.");
        return;
    }

    historialContainer.innerHTML = '<p>Cargando historial de pedidos...</p>';

    fetch(`${API_BASE_URL}/usuarios/${userId}/pedidos`)
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    const errorMessage = errorData.message || `Error ${response.status}: ${response.statusText}`;
                    throw new Error(errorMessage);
                });
            }
            return response.json();
        })
        .then(pedidos => {
            historialContainer.innerHTML = ''; // Limpiar contenido previo

            if (pedidos.length === 0) {
                historialContainer.innerHTML = '<p>No hay pedidos en el historial para este usuario.</p>';
                return;
            }

            pedidos.forEach(pedido => {
                const div = document.createElement('div');
                div.classList.add('pedido-historial-item');

                let productosEnPedidoHtml = '<ul>';
                if (pedido.lineaPedidos && Array.isArray(pedido.lineaPedidos)) {
                    pedido.lineaPedidos.forEach(linea => {
                        productosEnPedidoHtml += `<li>${linea.nombreProducto || 'Producto Desconocido'} x ${linea.cantidad} ($${linea.precioUnitario ? linea.precioUnitario.toFixed(2) : 'N/A'} c/u)</li>`;
                    });
                }
                productosEnPedidoHtml += '</ul>';

                div.innerHTML = `
                    <h4>Pedido #${pedido.id} - Fecha: ${new Date(pedido.fechaPedido).toLocaleString()}</h4>
                    <p>Estado: <strong>${pedido.estado || 'N/A'}</strong></p>
                    <p>Costo Total: <strong>$${pedido.costoTotal ? pedido.costoTotal.toFixed(2) : 'N/A'}</strong></p>
                    <p>Productos:</p>
                    ${productosEnPedidoHtml}
                `;
                historialContainer.appendChild(div);
            });
        })
        .catch(error => {
            historialContainer.innerHTML = `<p style="color: var(--accent-color);">Error al cargar el historial: ${error.message}.</p>`;
            console.error('Error al cargar historial de pedidos:', error);
            alert(`Error al cargar el historial de pedidos: ${error.message}`);
        });
}

// --- Event Listeners principales que se activan cuando el DOM está completamente cargado ---
document.addEventListener('DOMContentLoaded', () => {
    // Cargar productos automáticamente al cargar la página
    cargarProductos();

    // Evento para el botón de recargar productos (si aún lo necesitas además de la carga automática)
    const btnMostrar = document.getElementById('btnMostrar'); // Asumiendo que 'Recargar Productos' tiene el ID 'btnMostrar'
    if (btnMostrar) {
        btnMostrar.addEventListener('click', cargarProductos);
    }

    // Evento para el formulario de agregar producto
    const formAgregarProducto = document.getElementById('form-agregar-producto');
    if (formAgregarProducto) {
        formAgregarProducto.addEventListener('submit', agregarProducto);
    }

    // Eventos para el formulario de editar producto y botones del modal
    const formEditarProducto = document.getElementById('form-editar-producto');
    if (formEditarProducto) {
        formEditarProducto.addEventListener('submit', actualizarProducto);
        // Cerrar modal al hacer clic en 'X'
        const closeEditModalBtn = document.getElementById('close-edit-modal');
        if (closeEditModalBtn) {
            closeEditModalBtn.addEventListener('click', () => {
                const modal = document.getElementById('modal-editar-producto');
                if (modal) modal.style.display = 'none';
            });
        }
        // Cerrar modal al hacer clic en 'Cancelar'
        const cancelEditModalBtn = document.getElementById('cancel-edit-modal');
        if (cancelEditModalBtn) {
            cancelEditModalBtn.addEventListener('click', () => {
                const modal = document.getElementById('modal-editar-producto');
                if (modal) modal.style.display = 'none';
            });
        }
    }

    // Evento para el botón de crear pedido
    const btnCrearPedido = document.getElementById('btn-crear-pedido');
    if (btnCrearPedido) {
        btnCrearPedido.addEventListener('click', crearPedido);
    }

    // Evento para el botón de cargar historial de pedidos
    const btnCargarHistorial = document.getElementById('btn-cargar-historial');
    if (btnCargarHistorial) {
        btnCargarHistorial.addEventListener('click', cargarHistorialPedidos);
    }
});