package inventario;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Inventario {
    private final Map<String, Producto> productos = new LinkedHashMap<>();
    private double totalVentas;
    private double gananciaAcumulada;
    private int siguienteControl = 1;

    public void agregarProducto(Producto producto) {
        String control = String.format("INV-%05d", siguienteControl);
        producto.asignarControlInventario(control);
        productos.put(control, producto);
        siguienteControl++;
    }

    public Collection<Producto> listarProductos() {
        return productos.values();
    }

    public Producto buscarProducto(String controlInventario) {
        Producto producto = productos.get(controlInventario.trim());
        if (producto == null) {
            throw new IllegalArgumentException("No se encontro un producto con ese control de inventario.");
        }
        return producto;
    }

    public void reponerProducto(String controlInventario, int unidades) {
        buscarProducto(controlInventario).reponer(unidades);
    }

    public double venderProducto(String controlInventario, int unidades) {
        Producto producto = buscarProducto(controlInventario);
        producto.vender(unidades);
        double venta = producto.getPrecioVenta() * unidades;
        double ganancia = producto.calcularGanancia(unidades);
        totalVentas += venta;
        gananciaAcumulada += ganancia;
        return ganancia;
    }

    public double registrarPedido(Pedido pedido) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = buscarProducto(detalle.getControlProducto());
            if (detalle.getCantidad() > producto.getCantidad()) {
                throw new IllegalArgumentException("Existencias insuficientes para "
                        + producto.getNombre() + ". Disponibles: " + producto.getCantidad());
            }
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            buscarProducto(detalle.getControlProducto()).vender(detalle.getCantidad());
        }

        double total = pedido.calcularTotal();
        double ganancia = pedido.calcularGanancia();
        totalVentas += total;
        gananciaAcumulada += ganancia;
        return ganancia;
    }

    public int calcularUnidadesDisponibles() {
        return productos.values().stream().mapToInt(Producto::getCantidad).sum();
    }

    public double calcularValorDeCompra() {
        double valor = 0;
        for (Producto producto : productos.values()) {
            valor += producto.getPrecioCompra() * producto.getCantidad();
        }
        return valor;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public double getGananciaAcumulada() {
        return gananciaAcumulada;
    }
}