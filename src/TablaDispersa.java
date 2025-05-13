class TablaDispersa {
    private Tarea[] tabla;
    private int numElementos;
    private double factorCarga;
    private final int CAPACIDAD = 101;
    private final double A = 0.6180339887;

    // Constructor: cuando se crea la tabla, está vacía
    public TablaDispersa() {
        this.tabla = new Tarea[CAPACIDAD];
        this.numElementos = 0;
        this.factorCarga = 0.0;
    }
    // Este metodo calcula en que posicion se debe guardar una tarea  usando el codigo
    public int calcularPosicion(String codigo) {
        double valor = obtenerValorNumerico(codigo);  // convierte el codigo en un numero
        double producto = valor * A;
        double decimal = producto - Math.floor(producto); // nos quedamos con la parte decimal
        return (int) (decimal * CAPACIDAD); // lo pasamos a una posicion entre 0 y 100
    }

    // Convierte el codigo (texto) en un numero sumando los caracteres
    private int obtenerValorNumerico(String codigo) {
        String clave = codigo.length() > 10 ? codigo.substring(0, 10) : codigo;
        long suma = 0;
        for (char c : clave.toCharArray()) {
            suma += (int) c; // suma el valor ASCII de cada letra
        }
        return (int) (suma % Integer.MAX_VALUE); // se asegura de que no pase el limite
    }

    // Si hay una colision  usa esta formula para probar otras posiciones (sondeo cuadratico)
    public int resolverColision(int posicionInicial, int i) {
        return (posicionInicial + i * i) % CAPACIDAD;
    }

    // Inserta una tarea en la tabla  si hay espacio
    public boolean insertar(Tarea t) {
        if (numElementos >= CAPACIDAD) return false; // si esta llena  no se puede agregar

        int pos = calcularPosicion(t.getCodigo()); // calcula la posicion
        int i = 0;

        // busca una posicion libre usando el codigo
        while (tabla[pos] != null && tabla[pos].isAlta()) {
            // si ya existe una tarea con el mismo codigo, no se guarda
            if (tabla[pos].getCodigo().equals(t.getCodigo())) return false;
            pos = resolverColision(pos, ++i); // intenta otra posicion
        }

        // guarda la tarea y actualiza el contador
        tabla[pos] = t;
        numElementos++;
        calcularFactorCarga(); // actualiza el factor de carga
        return true;
    }

    // Busca una tarea por su nombre
    public Tarea buscarPorNombre(String nombre) {
        for (Tarea t : tabla) {
            if (t != null && t.isAlta() && t.getNombre().equalsIgnoreCase(nombre)) {
                return t;
            }
        }
        return null;  // si no se encuentra
    }
    // Elimina una tarea por su nombre (baja logica)
    public boolean eliminarPorNombre(String nombre) {
        for (Tarea t : tabla) {
            if (t != null && t.isAlta() && t.getNombre().equalsIgnoreCase(nombre)) {
                t.setAlta(false);// solo se marca como inactiva
                return true;
            }
        }
        return false;
    }
    // Calcula cuanto de la tabla está ocupada (entre 0 y 1)
    public double calcularFactorCarga() {
        this.factorCarga = (double) numElementos / CAPACIDAD;
        return factorCarga;
    }
    // Muestra todas las tareas activas de la tabla
    public void mostrarTabla() {
        System.out.println("\n=========== TABLA DE TAREAS ===========");
        for (int i = 0; i < tabla.length; i++) {
            Tarea t = tabla[i];
            if (t != null && t.isAlta()) {
                System.out.printf("\nPosición [%03d] ➤ %s\n", i, t.getNombre());
                System.out.println(t); // usa el metodo toString de la clase Tarea
            }
        }
        System.out.printf("=== Factor de carga: %.2f ===\n", calcularFactorCarga());
    }
}
