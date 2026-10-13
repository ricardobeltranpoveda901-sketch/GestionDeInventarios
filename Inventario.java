package inventario;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Inventario {
    private final Map<String, Producto> productos = new LinkedHashMap<>();
    private BigDecimal totalVentas = BigDecimal.ZERO;
    private BigDecimal gananciaAcumulada = BigDecimal.ZERO;
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

    public BigDecimal venderProducto(String controlInventario, int unidades) {
        Producto producto = buscarProducto(controlInventario);
        producto.vender(unidades);
        BigDecimal venta = producto.getPrecioVenta().multiply(BigDecimal.valueOf(unidades));
        BigDecimal ganancia = producto.calcularGanancia(unidades);
        totalVentas = totalVentas.add(venta);
        gananciaAcumulada = gananciaAcumulada.add(ganancia);
        return ganancia;
    }

    public BigDecimal registrarPedido(Pedido pedido) {
        if (pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe incluir al menos un producto.");
        }

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

        BigDecimal total = pedido.calcularTotal();
        BigDecimal ganancia = pedido.calcularGanancia();
        totalVentas = totalVentas.add(total);
        gananciaAcumulada = gananciaAcumulada.add(ganancia);
        return ganancia;
    }

    public int calcularUnidadesDisponibles() {
        return productos.values().stream().mapToInt(Producto::getCantidad).sum();
    }

    public BigDecimal calcularValorDeCompra() {
        return productos.values().stream()
                .map(producto -> producto.getPrecioCompra()
                        .multiply(BigDecimal.valueOf(producto.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    public BigDecimal getGananciaAcumulada() {
        return gananciaAcumulada;
    }
}