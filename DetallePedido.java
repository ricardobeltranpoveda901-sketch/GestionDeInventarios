package inventario;

public class DetallePedido {
    private final String controlProducto;
    private final String nombreProducto;
    private final double precioCompra;
    private final double precioVenta;
    private final int cantidad;

    public DetallePedido(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (producto.getControlInventario().isEmpty()) {
            throw new IllegalArgumentException("El producto debe registrarse en el inventario antes de agregarlo al pedido.");
        }
        this.controlProducto = producto.getControlInventario();
        this.nombreProducto = producto.getNombre();
        this.precioCompra = producto.getPrecioCompra();
        this.precioVenta = producto.getPrecioVenta();
        this.cantidad = cantidad;
    }

    public double calcularSubtotal() {
        return precioVenta * cantidad;
    }

    public double calcularGanancia() {
        return (precioVenta - precioCompra) * cantidad;
    }

    public String getControlProducto() {
        return controlProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }
}