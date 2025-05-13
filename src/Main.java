import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TablaDispersa tabla = new TablaDispersa();  // Estructura que almacena las tareas
        int opcion;


        do {
            System.out.println("\n----- MENU -----");
            System.out.println("1. Crear nueva tarea");
            System.out.println("2. Buscar tarea por nombre");
            System.out.println("3. Eliminar tarea por nombre");
            System.out.println("4. Mostrar todas las tareas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = Integer.parseInt(sc.nextLine().trim()); // se usa el .trim para

            switch (opcion) {
                case 1 -> {
                    Tarea nueva = Tarea.cargarDesdeTeclado(tabla);
                    boolean ok = tabla.insertar(nueva);
                    System.out.println(ok ? "Tarea insertada correctamente." : "No se pudo insertar la tarea.");
                }
                case 2 -> {
                    System.out.print("Ingrese el nombre de la tarea: ");
                    String nombre = sc.nextLine();
                    Tarea encontrada = tabla.buscarPorNombre(nombre);
                    if (encontrada != null) {
                        System.out.println("Tarea encontrada:\n" + encontrada);
                    } else {
                        System.out.println("Tarea no encontrada o dada de baja.");
                    }
                }
                case 3 -> {
                    System.out.print("Ingrese el nombre de la tarea a eliminar: ");
                    String nombre = sc.nextLine();
                    boolean eliminado = tabla.eliminarPorNombre(nombre);
                    System.out.println(eliminado ? "Tarea dada de baja." : "No se encontro la tarea.");
                }
                case 4 -> tabla.mostrarTabla();
                case 5 -> System.out.println("Saliendo del programa.");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 5);

        sc.close();
    }
}
