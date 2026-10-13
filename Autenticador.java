package inventario;

import java.util.HashMap;
import java.util.Map;

public class Autenticador {
    private final Map<String, Usuario> usuarios = new HashMap<>();

    public Autenticador() {
        usuarios.put("admin", new Usuario("admin", "admin123", RolUsuario.ADMINISTRADOR));
        usuarios.put("empleado", new Usuario("empleado", "empleado123", RolUsuario.EMPLEADO));
    }

    public Usuario autenticar(String nombreUsuario, String clave) {
        if (nombreUsuario == null || clave == null) {
            return null;
        }
        Usuario usuario = usuarios.get(nombreUsuario.trim());
        return usuario != null && usuario.validarClave(clave) ? usuario : null;
    }
}