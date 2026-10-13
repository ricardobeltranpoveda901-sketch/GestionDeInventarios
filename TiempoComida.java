package inventario;

public enum TiempoComida {
    DESAYUNO("Desayuno"),
    ALMUERZO("Almuerzo");

    private final String descripcion;

    TiempoComida(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}