package inventario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final Inventario inventario = new Inventario();
    private final Autenticador autenticador = new Autenticador();
    private final List<Pedido> pedidos = new ArrayList<>();
    private Usuario usuarioActual;
    private int siguienteNumeroPedido = 1;

    public static void main(String[] args) {
        new Main().iniciar();
    }

    private void iniciar() {
        System.out.println("=== Sistema para restaurantes ===");
        if (!iniciarSesion()) {
            scanner.close();
            return;
        }
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Selecciona una opcion: ");
            try {
                continuar = ejecutarOpcion(opcion);
            } catch (IllegalArgumentException | ArithmeticException excepcion) {
                System.out.println("Operacion no realizada: " + excepcion.getMessage());
            }
        }
        System.out.println("Hasta luego.");
        scanner.close();
    }

    private boolean iniciarSesion() {
        for (int intento = 1; intento <= 3; intento++) {
            String nombreUsuario = leerTexto("Usuario: ");
            String clave = leerTexto("Clave: ");
            usuarioActual = autenticador.autenticar(nombreUsuario, clave);
            if (usuarioActual != null) {
                System.out.println("Sesion iniciada: " + usuarioActual.getNombreUsuario()
                        + " (" + usuarioActual.getRol() + ").");
                return true;
            }
            System.out.println("Usuario o clave incorrectos. Intentos restantes: " + (3 - intento));
        }
        System.out.println("Se agotaron los intentos. Sesion cerrada.");
        return false;
    }

    private void mostrarMenu() {
        if (usuarioActual.getRol() == RolUsuario.ADMINISTRADOR) {
            System.out.println("\n1. Registrar producto");
            System.out.println("2. Añadir existencias");
            System.out.println("3. Consultar inventario");
        } else {
            System.out.println("\n1. Crear pedido");
            System.out.println("2. Consultar productos");
            System.out.println("3. Consultar pedidos");
            System.out.println("4. Ver resumen de ventas");
        }
        System.out.println("0. Salir");
    }

    private boolean ejecutarOpcion(int opcion) {
        if (opcion == 0) {
            return false;
        }

        if (usuarioActual.getRol() == RolUsuario.ADMINISTRADOR) {
            switch (opcion) {
                case 1 -> registrarProducto();
                case 2 -> reponerProducto();
                case 3 -> mostrarProductos();
                default -> System.out.println("Opcion no valida para administrador.");
            }
        } else {
            switch (opcion) {
                case 1 -> registrarPedido();
                case 2 -> mostrarProductos();
                case 3 -> mostrarPedidos();
                case 4 -> mostrarResumenVentas();
                default -> System.out.println("Opcion no valida para empleado.");
            }
        }
        return true;
    }

    private void registrarProducto() {
        String codigo = leerTexto("Codigo de referencia (opcional; Enter si no tiene): ");
        String nombre = leerTexto("Nombre: ");
        String categoria = leerTexto("Categoria: ");
        BigDecimal precioCompra = leerPrecio("Precio de compra: ");
        BigDecimal precioVenta = leerPrecio("Precio de venta: ");
        int cantidad = leerEnteroNoNegativo("Cantidad inicial: ");
        inventario.agregarProducto(new Producto(
                codigo, nombre, categoria, precioCompra, precioVenta, cantidad));
        System.out.println("Producto registrado.");
    }

    private void mostrarProductos() {
        if (inventario.listarProductos().isEmpty()) {
            System.out.println("El inventario esta vacio.");
            return;
        }
        System.out.println(String.join(" | ", "CONTROL", "COD. REF.", "NOMBRE",
                "CATEGORIA", "COMPRA", "VENTA", "STOCK"));
        for (Producto producto : inventario.listarProductos()) {
            System.out.println(String.join(" | ",
                    producto.getControlInventario(),
                    producto.getCodigo().isEmpty() ? "-" : producto.getCodigo(),
                    producto.getNombre(),
                    producto.getCategoria(),
                    dinero(producto.getPrecioCompra()),
                    dinero(producto.getPrecioVenta()),
                    String.valueOf(producto.getCantidad())));
        }
    }

    private void reponerProducto() {
        String codigo = leerTexto("Control del producto: ");
        int unidades = leerEnteroPositivo("Unidades a reponer: ");
        inventario.reponerProducto(codigo, unidades);
        System.out.println("Existencias actualizadas.");
    }

    private void registrarPedido() {
        if (inventario.listarProductos().isEmpty()) {
            System.out.println("No hay productos. Solicita al administrador que registre existencias.");
            return;
        }

        TipoPedido tipo = leerTipoPedido();
        TiempoComida tiempoComida = leerTiempoComida();
        CategoriaMenu categoriaMenu = leerCategoriaMenu();
        String referencia = tipo == TipoPedido.PARA_LLEVAR
                ? "Sin referencia"
                : leerTexto(tipo == TipoPedido.MESA ? "Numero de mesa: " : "Direccion de entrega: ");
        String codigoPedido = String.format("PED-%04d", siguienteNumeroPedido);
        Pedido pedido = new Pedido(codigoPedido, tipo, tiempoComida, categoriaMenu, referencia,
                usuarioActual.getNombreUsuario());

        while (true) {
            mostrarProductos();
            System.out.print("Control del producto (Enter para terminar): ");
            String codigoProducto = scanner.nextLine().trim();
            if (codigoProducto.isEmpty()) {
                break;
            }
            Producto producto = inventario.buscarProducto(codigoProducto);
            int cantidad = leerEnteroPositivo("Cantidad: ");
            pedido.agregarProducto(producto, cantidad);
            System.out.println("Producto agregado al pedido.");
        }

        if (pedido.getDetalles().isEmpty()) {
            System.out.println("Pedido cancelado: no se agregaron productos.");
            return;
        }

        BigDecimal ganancia = inventario.registrarPedido(pedido);
        pedidos.add(pedido);
        siguienteNumeroPedido++;
        System.out.println("Pedido " + pedido.getCodigo() + " registrado por "
                + dinero(pedido.calcularTotal()) + ". Resultado: " + dinero(ganancia));
    }

    private TipoPedido leerTipoPedido() {
        while (true) {
            System.out.println("Tipo de pedido: 1. En mesa  2. Para llevar  3. A domicilio");
            int opcion = leerEntero("Selecciona el tipo: ");
            switch (opcion) {
                case 1:
                    return TipoPedido.MESA;
                case 2:
                    return TipoPedido.PARA_LLEVAR;
                case 3:
                    return TipoPedido.DOMICILIO;
                default:
                    System.out.println("Selecciona 1, 2 o 3.");
            }
        }
    }

    private TiempoComida leerTiempoComida() {
        while (true) {
            System.out.println("Menu para: 1. Desayuno  2. Almuerzo");
            int opcion = leerEntero("Selecciona la jornada: ");
            switch (opcion) {
                case 1:
                    return TiempoComida.DESAYUNO;
                case 2:
                    return TiempoComida.ALMUERZO;
                default:
                    System.out.println("Selecciona 1 o 2.");
            }
        }
    }

    private CategoriaMenu leerCategoriaMenu() {
        while (true) {
            System.out.println("Tipo de menu: 1. Menu del dia  2. Corriente  3. Ejecutivo");
            int opcion = leerEntero("Selecciona el menu: ");
            switch (opcion) {
                case 1:
                    return CategoriaMenu.MENU_DEL_DIA;
                case 2:
                    return CategoriaMenu.CORRIENTE;
                case 3:
                    return CategoriaMenu.EJECUTIVO;
                default:
                    System.out.println("Selecciona 1, 2 o 3.");
            }
        }
    }

    private void mostrarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("Todavia no hay pedidos registrados.");
            return;
        }
        for (Pedido pedido : pedidos) {
            String tipoReferencia = pedido.getTipo() == TipoPedido.MESA ? "Mesa" : "Referencia";
            System.out.println(String.join(" | ",
                    pedido.getCodigo(),
                    pedido.getTipo().getDescripcion(),
                        "Menu: " + pedido.getTiempoComida().getDescripcion() + " - "
                            + pedido.getCategoriaMenu().getDescripcion(),
                    tipoReferencia + ": " + pedido.getReferencia(),
                    "Atendio: " + pedido.getAtendidoPor(),
                    "Total: " + dinero(pedido.calcularTotal())));
            for (DetallePedido detalle : pedido.getDetalles()) {
                System.out.println("  " + detalle.getCantidad() + " x "
                        + detalle.getNombreProducto() + " ("
                        + dinero(detalle.calcularSubtotal()) + ")");
            }
        }
    }

    private void mostrarResumenVentas() {
        BigDecimal ganancia = inventario.getGananciaAcumulada();
        String resultado = ganancia.signum() < 0 ? "Perdida acumulada" : "Ganancia acumulada";
        System.out.println("Pedidos registrados: " + pedidos.size());
        System.out.println("Total de ventas: " + dinero(inventario.getTotalVentas()));
        System.out.println(resultado + ": " + dinero(ganancia.abs()));
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException excepcion) {
                System.out.println("Ingresa un numero entero valido.");
            }
        }
    }

    private int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor > 0) {
                return valor;
            }
            System.out.println("El valor debe ser mayor que cero.");
        }
    }

    private int leerEnteroNoNegativo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= 0) {
                return valor;
            }
            System.out.println("El valor no puede ser negativo.");
        }
    }

    private BigDecimal leerPrecio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                BigDecimal precio = new BigDecimal(scanner.nextLine().trim().replace(',', '.'));
                if (precio.signum() >= 0) {
                    return precio;
                }
            } catch (NumberFormatException excepcion) {
                System.out.println("Ingresa un precio valido, por ejemplo 12.50.");
                continue;
            }
            System.out.println("El precio no puede ser negativo.");
        }
    }

    private String dinero(BigDecimal valor) {
        return "$" + valor.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }
}