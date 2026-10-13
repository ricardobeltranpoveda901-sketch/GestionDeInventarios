package inventario;

public enum CategoriaMenu {
    MENU_DEL_DIA("Menu del dia"),
    CORRIENTE("Corriente"),
    EJECUTIVO("Ejecutivo");

    private final String descripcion;

    CategoriaMenu(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}