package inventario;

public class Producto {
    private final String codigo;
    private final String nombre;
    private final String categoria;
    private final double precioCompra;
    private final double precioVenta;
    private String controlInventario = "";
    private int cantidad;

    public Producto(String codigo, String nombre, String categoria,
                    double precioCompra, double precioVenta, int cantidad) {
        this.codigo = codigo == null ? "" : codigo.trim();
        this.nombre = validarTexto(nombre, "El nombre");
        this.categoria = validarTexto(categoria, "La categoria");
        if (precioCompra < 0 || precioVenta < 0) {
            throw new IllegalArgumentException("Los precios no pueden ser negativos.");
        }
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
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

    public double calcularGanancia(int unidades) {
        return (precioVenta - precioCompra) * unidades;
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

    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public int getCantidad() {
        return cantidad;
    }
}