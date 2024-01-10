package CruzUziel;
/**
 * Clase que resuelve el problema de adoquinamiento
 * El problema consiste en utilizar adoquines de tamaño 3 en forma de L.
 * Para cubrir completamente una superficie de un área de nxn = 2^k
 * La cuadricula inicial comienza con un adoquín especial que 
 * ocupa una sola localidad en la matriz, el cual no puede ser tapada por algún adoquín en forma de L.
 */
public class Adoquinador {

    private int[][] regionR; // Matriz para la región R
    private int m; // Tamaño de la región R
    private int siguienteNumeroAdoquin = 1; // Variable para el número del próximo adoquín
    private int[][] adoquinL = {{1, 0}, {1, 1}}; // Matriz para el adoquín L

    /**
     * Constructor de la clase
     * @param m Longitud de la región R
     */
    public Adoquinador(int m) {
        this.m = m;
        this.regionR = new int[m][m];
    }

    /**
     * Método inicia el proceso de colocación de adoquines en la región. 
     * Luego, llama al método colocarAdoquin para comenzar la recursión.
     * @return true si se pudo colocar todos los adoquines, false en caso contrario
     */
    public boolean colocarAdoquines() {
        return colocarAdoquin(0, 0);
    }

    /**
     * Método recursivo que coloca los adoquines en la región R
     * Utiliza la recursión para explorar diferentes orientaciones del adoquín y verifica si es posible colocarlo en la posición actual.
     * Si es posible, lo coloca y continúa con la siguiente posición.
     * @param x Coordenada x de la posición actual
     * @param y Coordenada y de la posición actual
     * @return true si se pudo colocar todos los adoquines, false en caso contrario
     */
    private boolean colocarAdoquin(int x, int y) {
        if (x >= m) {
            // Hemos colocado todos los adoquines
            return true;
        }

        // Si ya hay un cuadrado en esta posición, pasa a la siguiente
        if (regionR[x][y] != 0) {
            return colocarAdoquin((y == m - 1) ? x + 1 : x, (y == m - 1) ? 0 : y + 1);
        }

        // Intenta colocar el adoquín en esta posición y en sus rotaciones
        for (int orientacion = 0; orientacion < 4; orientacion++) {
            if (esPosibleColocarAdoquin(x, y)) {
                // Coloca el adoquín y asigna el número correspondiente
                int numeroAdoquin = siguienteNumeroAdoquin;
                for (int i = 0; i < adoquinL.length; i++) {
                    for (int j = 0; j < adoquinL[i].length; j++) {
                        if (adoquinL[i][j] == 1) {
                            regionR[x + i][y + j] = numeroAdoquin;
                        }
                    }
                }

                // Incrementa el número del próximo adoquín
                siguienteNumeroAdoquin++;

                // Llama recursivamente para la siguiente posición
                int nextX = (y == m - 1) ? x + 1 : x;
                int nextY = (y == m - 1) ? 0 : y + 1;
                if (colocarAdoquin(nextX, nextY)) {
                    return true;
                }

                // Si no se pudo colocar en esta posición, retrocede y prueba otra orientación
                for (int i = 0; i < adoquinL.length; i++) {
                    for (int j = 0; j < adoquinL[i].length; j++) {
                        if (adoquinL[i][j] == 1) {
                            regionR[x + i][y + j] = 0;
                        }
                    }
                }
            }

            // Rota el adoquín
            adoquinL = rotarAdoquin(adoquinL);
        }

        return false;
    }

    /**
     * Método que verifica si es posible colocar un adoquín en una posición determinada
     * sin superponer cuadrados especiales o salirse de los límites de la región.
     * @param x Coordenada x de la posición actual
     * @param y Coordenada y de la posición actual
     * @return true si es posible colocar el adoquín, false en caso contrario
     */
    private boolean esPosibleColocarAdoquin(int x, int y) {
        int n = adoquinL.length;

        // Verifica que el adoquín no se salga de la región
        if (x + n > m || y + n > m) {
            return false;
        }

        // Verifica que no se superpongan cuadrados especiales
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (adoquinL[i][j] == 1 && regionR[x + i][y + j] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Método que rota un adoquín en sentido horario
     * @param matriz Matriz que representa el adoquín
     * @return Matriz que representa el adoquín rotado
     */
    private int[][] rotarAdoquin(int[][] matriz) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        int[][] resultado = new int[columnas][filas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                resultado[j][filas - i - 1] = matriz[i][j];
            }
        }

        return resultado;
    }


    /**
     * Método que muestra la región resultante
     * Imprime la matriz de la región resultante con los números de los adoquines
     * Calcula la longitud del número más grarnde de la región para determinar cuántos espacios
     * en blanco se deben imprimir para que la matriz se vea ordenada.
     * Utilizamos el método String.repeat() para repetir un espacio en blanco la cantidad de veces necesaria.
     */
    public void mostrarRegionResultante() {
        int maxNumLength = String.valueOf(siguienteNumeroAdoquin - 1).length();
    
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                String numStr = String.valueOf(regionR[i][j]);
                int numSpaces = maxNumLength - numStr.length();
                String spaces = " ".repeat(numSpaces);
    
                System.out.print(numStr + spaces + " ");
            }
            System.out.println();
        }
    }
    

    /**
     * Método main 
     * Recibe como entrrada en los argumenos de la linea de comandos 
     * un entero positivo k, el cuál indica el tamaño de la matriz de la región R potencia de 2.
     * @param args Argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java Adoquinador <k>");
            System.exit(1);
        }
    
        try {
            int k = Integer.parseInt(args[0]);
            int m = (int) Math.pow(2, k); // Calcula el tamaño de la región m a partir de k
    
            if (k < 1 || k > 10) {
                System.out.println("El valor de k debe estar entre 1 y 10.");
                System.exit(1);
            }
    
            Adoquinador adoquinador = new Adoquinador(m);
            boolean exito = adoquinador.colocarAdoquines();
    
            if (exito) {
                System.out.println("Se ha completado la colocación de adoquines en la región.");
                adoquinador.mostrarRegionResultante();
            } else {
                System.out.println("No se pudo colocar el adoquín en la región.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingresa un valor válido para k.");
        }
    }
    
}

