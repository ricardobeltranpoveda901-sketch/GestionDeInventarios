package inventario;

public enum TipoPedido {
    MESA("En mesa"),
    PARA_LLEVAR("Para llevar"),
    DOMICILIO("A domicilio");

    private final String descripcion;

    TipoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}