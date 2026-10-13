package inventario;

public class MenuRestaurante {
    private final String nombre;
    private final TiempoComida tiempoComida;
    private final CategoriaMenu categoria;
    private final double precio;

    public MenuRestaurante(String nombre, TiempoComida tiempoComida,
                           CategoriaMenu categoria, double precio) {
        this.nombre = nombre;
        this.tiempoComida = tiempoComida;
        this.categoria = categoria;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public TiempoComida getTiempoComida() {
        return tiempoComida;
    }

    public CategoriaMenu getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }
}