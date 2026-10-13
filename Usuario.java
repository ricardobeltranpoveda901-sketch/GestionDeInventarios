package inventario;

public class Usuario {
    private final String nombreUsuario;
    private final String clave;
    private final RolUsuario rol;

    public Usuario(String nombreUsuario, String clave, RolUsuario rol) {
        this.nombreUsuario = validarTexto(nombreUsuario, "El usuario");
        this.clave = validarTexto(clave, "La clave");
        this.rol = rol;
    }

    private static String validarTexto(String valor, String campo) {
        String texto = valor.trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        return texto;
    }

    boolean validarClave(String claveIngresada) {
        return clave.equals(claveIngresada);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public RolUsuario getRol() {
        return rol;
    }
}