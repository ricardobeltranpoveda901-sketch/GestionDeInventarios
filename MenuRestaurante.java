package inventario;

import java.math.BigDecimal;

public class MenuRestaurante {
    private final String nombre;
    private final TiempoComida tiempoComida;
    private final CategoriaMenu categoria;
    private final BigDecimal precio;

    public MenuRestaurante(String nombre, TiempoComida tiempoComida,
                           CategoriaMenu categoria, BigDecimal precio) {
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

    public BigDecimal getPrecio() {
        return precio;
    }
}