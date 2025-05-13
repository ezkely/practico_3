import java.util.UUID;
import java.time.LocalDate;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Tarea {
    // Identificador único generado automáticamente para garantizar unicidad sin intervención del usuario
    private String codigo;


    private String nombre;
    private String descripcion;

    private int estado; // 1 = pendiente, 2 = en progreso, 3 = finalizada

    // Fechas con control de consistencia (inicio <= fin)
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Marca lógica para indicar si la tarea está activa (true) o dada de baja (false)
    private boolean esAlta;

    // Constructor sin nada  solo genera el codigo y marca que está activa
    public Tarea() {
        this.codigo = UUID.randomUUID().toString();
        this.esAlta = true;
    }


    // Constructor con datos básicos de la tarea
    public Tarea(String nombre, String descripcion, int estado) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.esAlta = true;
    }

    // Este metodo es para cargar una tarea escribiendo por consola
    public static Tarea cargarDesdeTeclado(TablaDispersa tabla) {
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String nombre = "";
        // Pide el nombre hasta que no este vacio y no se repita
        while (nombre.isBlank()) {
            System.out.print("Ingrese el nombre: ");
            nombre = sc.nextLine().trim();
            if (nombre.isBlank()) {
                System.out.println("El nombre no puede estar vacío.");
            } else if (tabla.buscarPorNombre(nombre) != null) {
                System.out.println("Ya existe una tarea con ese nombre. Ingrese un nombre diferente.");
                nombre = "";
            }
        }

        String descripcion;
        // Pide la descripcion  no puede pasar los 200 caracteres
        do {
            System.out.print("Ingrese la descripción (opcional, máximo 200 caracteres): ");
            descripcion = sc.nextLine().trim();
            if (descripcion.length() > 200) {
                System.out.println("La descripción es demasiado larga. Intente nuevamente.");
            }
        } while (descripcion.length() > 200);

        int estado = 0;

        // Pide el estado solo permite 1 o 2
        while (estado != 1 && estado != 2) {
            System.out.print("Ingrese el estado (1 = pendiente, 2 = en progreso): ");
            try {
                estado = Integer.parseInt(sc.nextLine().trim());
                if (estado != 1 && estado != 2) {
                    System.out.println("Estado inválido. Solo se permiten 1 o 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número entero.");
            }
        }

        LocalDate fechaInicio = null;
        // Pide la fecha de inicio, tiene que ser hoy o despues
        while (fechaInicio == null) {
            try {
                System.out.print("Ingrese la fecha de inicio (YYYY-MM-DD): ");
                String input = sc.nextLine().trim();
                fechaInicio = LocalDate.parse(input, formatter);
                if (fechaInicio.isBefore(LocalDate.now())) {
                    System.out.println("La fecha de inicio no puede ser anterior a hoy.");
                    fechaInicio = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Intente de nuevo con formato YYYY-MM-DD.");
            }
        }

        LocalDate fechaFin = null;
        // Pide la fecha de fin, no puede ser antes de la de inicio
        while (fechaFin == null) {
            try {
                System.out.print("Ingrese la fecha de fin (YYYY-MM-DD): ");
                String input = sc.nextLine().trim();
                fechaFin = LocalDate.parse(input, formatter);
                if (fechaFin.isBefore(fechaInicio)) {
                    System.out.println("La fecha de fin no puede ser anterior a la fecha de inicio.");
                    fechaFin = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Intente de nuevo con formato YYYY-MM-DD.");
            }
        }


        // crea tarea con todo lo que el usuario escribio

        Tarea tarea = new Tarea(nombre, descripcion, estado);
        tarea.setFechaInicio(fechaInicio);
        tarea.setFechaFin(fechaFin);
        return tarea;
    }


    //Los getters y setters
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getEstado() { return estado; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public boolean isAlta() { return esAlta; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public void setAlta(boolean esAlta) { this.esAlta = esAlta; }

    // Este metodo convierte los datos de la tarea en texto para poder mostrarla
    @Override
    public String toString() {
        // Según el numero del estado  muestra texto mas facil de leer
        String estadoTexto = switch (estado) {
            case 1 -> "pendiente";
            case 2 -> "en progreso";
            case 3 -> "finalizada";
            default -> "desconocido";
        };
        // Devuelve los datos de la tarea ordenados con saltos de linea
        return """
----------------------------------------
Tarea
----------------------------------------
Nombre       : %s
Código       : %s
Descripcion  : %s
Estado       : %s
Fecha Inicio : %s
Fecha Fin    : %s
----------------------------------------
""".formatted(
                nombre,
                codigo,
                descripcion,
                estadoTexto,
                fechaInicio,
                fechaFin
        );
    }
}