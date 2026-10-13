package inventario;

import java.math.BigDecimal;

public class Producto {
    private final String codigo;
    private final String nombre;
    private final String categoria;
    private final BigDecimal precioCompra;
    private final BigDecimal precioVenta;
    private String controlInventario = "";
    private int cantidad;

    public Producto(String codigo, String nombre, String categoria,
                    BigDecimal precioCompra, BigDecimal precioVenta, int cantidad) {
        this.codigo = codigo == null ? "" : codigo.trim();
        this.nombre = validarTexto(nombre, "El nombre");
        this.categoria = validarTexto(categoria, "La categoria");
        this.precioCompra = validarPrecio(precioCompra, "El precio de compra");
        this.precioVenta = validarPrecio(precioVenta, "El precio de venta");
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        this.cantidad = cantidad;
    }

    private static String validarTexto(String valor, String campo) {
        String texto = valor.trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        return texto;
    }

    private static BigDecimal validarPrecio(BigDecimal precio, String campo) {
        if (precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(campo + " no puede ser negativo.");
        }
        return precio;
    }

    void asignarControlInventario(String controlInventario) {
        if (!this.controlInventario.isEmpty()) {
            throw new IllegalStateException("El producto ya pertenece a un inventario.");
        }
        this.controlInventario = validarTexto(controlInventario, "El control de inventario");
    }

    public void reponer(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades deben ser mayores que cero.");
        }
        cantidad = Math.addExact(cantidad, unidades);
    }

    public void vender(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades deben ser mayores que cero.");
        }
        if (unidades > cantidad) {
            throw new IllegalArgumentException("Existencias insuficientes. Disponibles: " + cantidad);
        }
        cantidad -= unidades;
    }

    public BigDecimal calcularGanancia(int unidades) {
        return precioVenta.subtract(precioCompra).multiply(BigDecimal.valueOf(unidades));
    }

    public String getCodigo() {
        return codigo;
    }

    public String getControlInventario() {
        return controlInventario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public int getCantidad() {
        return cantidad;
    }
}