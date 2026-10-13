package inventario;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final String codigo;
    private final TipoPedido tipo;
    private final MenuRestaurante menu;
    private final String referencia;
    private final String atendidoPor;
    private final List<DetallePedido> detalles = new ArrayList<>();

    public Pedido(String codigo, TipoPedido tipo, MenuRestaurante menu,
                  String referencia, String atendidoPor) {
        this.codigo = validarTexto(codigo, "El codigo del pedido");
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de pedido es obligatorio.");
        }
        this.tipo = tipo;
        this.menu = menu;
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

    public double calcularTotal() {
        double total = menu.getPrecio();
        for (DetallePedido detalle : detalles) {
            total += detalle.calcularSubtotal();
        }
        return total;
    }

    public double calcularGanancia() {
        double ganancia = 0;
        for (DetallePedido detalle : detalles) {
            ganancia += detalle.calcularGanancia();
        }
        return ganancia;
    }

    public String getCodigo() {
        return codigo;
    }

    public TipoPedido getTipo() {
        return tipo;
    }

    public MenuRestaurante getMenu() {
        return menu;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getAtendidoPor() {
        return atendidoPor;
    }
}