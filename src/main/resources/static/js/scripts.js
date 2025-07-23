const API_BASE_URL = 'http://localhost:8080/api';

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
            contenedor.innerHTML = '';

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
            attachProductActionListeners();
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

function agregarProducto(event) {
    event.preventDefault();

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
        form.reset();
        cargarProductos();
    })
    .catch(error => {
        console.error('Error al agregar producto:', error);
        alert(`Error al agregar producto: ${error.message}`);
    });
}

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
        cargarProductos();
    })
    .catch(error => {
        console.error('Error al eliminar producto:', error);
        alert(`Error al eliminar producto: ${error.message}`);
    });
}

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
            document.getElementById('edit-productId').value = producto.id;
            document.getElementById('edit-nombre').value = producto.nombre;
            document.getElementById('edit-descripcion').value = producto.descripcion;
            document.getElementById('edit-precio').value = producto.precio;
            document.getElementById('edit-categoria').value = producto.categoria;
            document.getElementById('edit-imagenUrl').value = producto.imagenUrl;
            document.getElementById('edit-stock').value = producto.stock;

            const modal = document.getElementById('modal-editar-producto');
            if (modal) modal.style.display = 'block';
        })
        .catch(error => {
            console.error('Error al cargar producto para edición:', error);
            alert(`No se pudo cargar el producto para edición: ${error.message}`);
        });
}

function actualizarProducto(event) {
    event.preventDefault();

    const form = document.getElementById('form-editar-producto');
    if (!form) return;

    const productId = parseInt(form.elements['edit-productId'].value, 10);

    const productoActualizado = {
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
        if (modal) modal.style.display = 'none';
        cargarProductos();
    })
    .catch(error => {
        console.error('Error al actualizar producto:', error);
        alert(`Error al actualizar producto: ${error.message}`);
    });
}

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

function crearPedido() {
    if (carrito.length === 0) {
        alert('El carrito está vacío. Agrega productos antes de realizar un pedido.');
        return;
    }

    const lineas = carrito.map(item => ({
        producto: {id: item.productoId}, // Modificado para LineaPedidoRequest.ProductoRequestId
        cantidad: item.cantidad
    }));

    const pedidoRequest = {
        lineas: lineas
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
        carrito = [];
        actualizarVistaCarrito();
        cargarProductos();
    })
    .catch(error => {
        console.error('Error al crear pedido:', error);
        alert(`Error al crear pedido: ${error.message}. Asegúrate de tener stock suficiente y el carrito con productos.`);
    });
}

function cargarHistorialPedidos() {
    const historialContainer = document.getElementById('historial-pedidos-container');

    if (!historialContainer) {
        console.error("El elemento 'historial-pedidos-container' no se encontró en el DOM.");
        return;
    }

    historialContainer.innerHTML = '<p>Cargando historial de pedidos...</p>';

    // Nota: El backend actualmente devuelve *todos* los pedidos. Si necesitas pedidos por usuario,
    // el backend requeriría un endpoint específico como /api/usuarios/{userId}/pedidos.
    fetch(`${API_BASE_URL}/pedidos`)
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
            historialContainer.innerHTML = '';

            if (pedidos.length === 0) {
                historialContainer.innerHTML = '<p>No hay pedidos en el historial.</p>';
                return;
            }

            pedidos.forEach(pedido => {
                const div = document.createElement('div');
                div.classList.add('pedido-historial-item');

                let productosEnPedidoHtml = '<ul>';
                if (pedido.lineas && Array.isArray(pedido.lineas)) {
                    pedido.lineas.forEach(linea => {
                        productosEnPedidoHtml += `<li>${linea.nombreProducto || 'Producto Desconocido'} x ${linea.cantidad} ($${linea.precioUnitario ? linea.precioUnitario.toFixed(2) : 'N/A'} c/u)</li>`;
                    });
                }
                productosEnPedidoHtml += '</ul>';

                div.innerHTML = `
                    <h4>Pedido #${pedido.id} - Fecha: ${new Date(pedido.fecha).toLocaleString()}</h4>
                    <p>Estado: <strong>${pedido.estado || 'N/A'}</strong></p>
                    <p>Costo Total: <strong>$${pedido.total ? pedido.total.toFixed(2) : 'N/A'}</strong></p>
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

document.addEventListener('DOMContentLoaded', () => {
    cargarProductos();

    const btnMostrar = document.getElementById('btnMostrar');
    if (btnMostrar) {
        btnMostrar.addEventListener('click', cargarProductos);
    }

    const formAgregarProducto = document.getElementById('form-agregar-producto');
    if (formAgregarProducto) {
        formAgregarProducto.addEventListener('submit', agregarProducto);
    }

    const formEditarProducto = document.getElementById('form-editar-producto');
    if (formEditarProducto) {
        formEditarProducto.addEventListener('submit', actualizarProducto);
        const closeEditModalBtn = document.getElementById('close-edit-modal');
        if (closeEditModalBtn) {
            closeEditModalBtn.addEventListener('click', () => {
                const modal = document.getElementById('modal-editar-producto');
                if (modal) modal.style.display = 'none';
            });
        }
        const cancelEditModalBtn = document.getElementById('cancel-edit-modal');
        if (cancelEditModalBtn) {
            cancelEditModalBtn.addEventListener('click', () => {
                const modal = document.getElementById('modal-editar-producto');
                if (modal) modal.style.display = 'none';
            });
        }
    }

    const btnCrearPedido = document.getElementById('btn-crear-pedido');
    if (btnCrearPedido) {
        btnCrearPedido.addEventListener('click', crearPedido);
    }

    const btnCargarHistorial = document.getElementById('btn-cargar-historial');
    if (btnCargarHistorial) {
        btnCargarHistorial.addEventListener('click', cargarHistorialPedidos);
    }
});