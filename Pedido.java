package inventario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final String codigo;
    private final TipoPedido tipo;
    private final TiempoComida tiempoComida;
    private final CategoriaMenu categoriaMenu;
    private final String referencia;
    private final String atendidoPor;
    private final List<DetallePedido> detalles = new ArrayList<>();

    public Pedido(String codigo, TipoPedido tipo, TiempoComida tiempoComida,
                  CategoriaMenu categoriaMenu, String referencia, String atendidoPor) {
        this.codigo = validarTexto(codigo, "El codigo del pedido");
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de pedido es obligatorio.");
        }
        this.tipo = tipo;
        this.tiempoComida = tiempoComida;
        this.categoriaMenu = categoriaMenu;
        this.referencia = validarTexto(referencia, "La referencia");
        this.atendidoPor = validarTexto(atendidoPor, "El empleado");
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        String texto = valor.trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        return texto;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        DetallePedido nuevoDetalle = new DetallePedido(producto, cantidad);
        for (int indice = 0; indice < detalles.size(); indice++) {
            DetallePedido detalle = detalles.get(indice);
            if (detalle.getControlProducto().equals(producto.getControlInventario())) {
                int cantidadActualizada = Math.addExact(detalle.getCantidad(), cantidad);
                detalles.set(indice, new DetallePedido(producto, cantidadActualizada));
                return;
            }
        }
        detalles.add(nuevoDetalle);
    }

    public List<DetallePedido> getDetalles() {
        return List.copyOf(detalles);
    }

    public BigDecimal calcularTotal() {
        return detalles.stream().map(DetallePedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularGanancia() {
        return detalles.stream().map(DetallePedido::calcularGanancia)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getCodigo() {
        return codigo;
    }

    public TipoPedido getTipo() {
        return tipo;
    }

    public TiempoComida getTiempoComida() {
        return tiempoComida;
    }

    public CategoriaMenu getCategoriaMenu() {
        return categoriaMenu;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getAtendidoPor() {
        return atendidoPor;
    }
}