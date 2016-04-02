package clemaitre;

import java.io.Serializable;
import java.util.*;

/**
 *
 * Clase que implementa un encriptador que utiliza cifradorAES
 *
 * @author Esteban Hernández y Camilo Benavides
 */
public class cifradorAES implements Serializable {

    private int rondas = 14; // Ciclos de repetición para la transformación de claves. 14 son para llaves de 256 bits cifradorAES has 10-14 rounds
    private int tamBloque = 16; // Tamaño del bloque de la llave de cifradorAES (128 bits/ 16 Bytes).
    private int tamClave = 32;    // Longitud de las claves (128/192/256 bits - 16/24/32 bytes)

    /**
     * Numero de rondas usados por la clave de cifrado de cifradorAES. (10-14
     * rondas)
     */
    private int numRondas;
    /**
     * Clave de encripción por rondas derivadas de la clave inicial expandida
     */
    private byte[][] llaveEncriptar;
    /**
     * Clave de desencripción por rondas derivadas de la clave inicial expandida
     *
     */
    private byte[][] llaveDesencriptar;

    /**
     * Tabla de encriptación de cifradorAES. Esta tabla se utiliza como la tabla
     * de Sustitución de Bytes Los valores se han dejado en decimal en vez de
     * hexadecimal Y en forma de arreglo en vez de matriz para ubicar mas rapido
     * los valores (Ya que al manejar decimal en vez de hexadecimal, el indice
     * ya no se realiza con matriz sino de manera unidimensional) Los negativos
     * son generados al sobrepasar el tamaño máximo del byte: 127 Desde el
     * hexadecimal 80 hasta el FF van dese -128 a -1, respectivamente. Conversor
     * de hexadecimal a decimal:
     * http://www.rapidtables.com/convert/number/hex-to-decimal.htm Tabla SBox
     * de referencia http://www.samiam.org/s-box.html
     */
    private static byte[] SBox = {
        99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, // Fila 0
        -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, // Fila 1
        -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, //Fila 2
        4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, //Fila 3
        9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, // Fila 4
        83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, //Fila 5
        -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, // Fila 6
        81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, //Fila 7
        -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, // Fila 8
        96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37,//Fila 9
        -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, //Fila A
        -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8,//Fila B
        -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118,//Fila C
        112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, //Fila D
        -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33,//Fila E
        -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};//Fila F

    /**
     * Tabla de desencriptación de cifradorAES. Esta tabla se utiliza como la
     * tabla de Sustitución de Bytes Los valores se han dejado en decimal en vez
     * de hexadecimal Y en forma de arreglo en vez de matriz para ubicar mas
     * rapido los valores Los negativos son generados al sobrepasar el tamaño
     * máximo del byte: 127 Desde el hexadecimal 80 hasta el FF van dese -128 a
     * -1, respectivamente. Conversor de hexadecimal a decimal:
     * http://www.rapidtables.com/convert/number/hex-to-decimal.htm Tabla SBox
     * de referencia http://www.samiam.org/s-box.html
     */
    private static byte[] SdBox = {
        82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5,//Fila 0
        124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53,//Fila 1
        84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78,//Fila 2
        8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37,//Fila 3
        114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110,//Fila 4
        108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124,//Fila 5
        -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6,//Fila 6
        -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, //Fila 7
        58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115,//Fila 8
        -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110,//Fila 9
        71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27,//Fila A
        -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12,//Fila B
        31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95,//Fila C
        96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17,//Fila D
        -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97,//Fila E
        23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125};//Fila F

    /**
     * Tabla de constante de rondas de claves cifradorAES Los valores de la
     * tabla rcon se utilizan por cada columna multiplo de 4 en la clave
     * expandida El rcon solo afecta la primera fila, por lo que no se ha
     * expandido como matriz el resto de las filas son ceros.
     */
    private byte[] rcon = {0, 1, 2, 4, 8, 16, 32, 64, -128, 27, 54, 108, -40, -85, 77, -102, 47, 94, -68, 99, -58, -105, 53, 106, -44, -77, 125, -6, -17, -59, -111};
    private int numFilas = 4; // Numero de filas por columnas en la variable de estado del cifradorAES
    private int numCols = tamBloque / numFilas; // number of columns in cifradorAES state variable

    /**
     * se define el numero de rotaciones de byts para cada fila (Shiftrows).
     */
    private int[] filasShift = {0, 1, 2, 3};//0 rotaciones en la primera fila; 1 en la segunda; 2 en la tercera, 3 en la cuarta

    /**
     * Rijndael Implementa un Campo de Galois que es un campo matricial donde
     * las operaciones básicas son redefinidas para un conjunto de enteros de 0
     * a 255 (256 números en total). http://www.samiam.org/galois.html Toda
     * operacion siempre resulta en un numero de 8 bits En el producto, se puede
     * multiplicar un numero por si mismo tantas veces que se puede llegar a
     * cualquier otro de los 255 valores posibles, aunque solo unos numero
     * cumplen de esta propiedad, llamados generadores
     *
     * Utilizar multiplicaciones repetidas son demasiado dispendiosas Por lo que
     * se utiliza una tabla de logaritmo de 256 bytes Y una tabla de
     * exponenciación de 256 bytes
     *
     */
    private int[] expTable = new int[256];
    private int[] logTable = new int[256];
    private int raizGenerador = 0x11B;// Generador del polinomio del Campo Galois en 2^8

    /**
     * Constructor vacío del AES Con los otros métodos se genera la estructura
     * del AES
     */
    public cifradorAES() {
        prepararTablas();
    }

    /**
     * Generación de las tablas de exponentes y logaritmos Con esta tabla se
     * realizan las multiplicaciones en el campo galois en 2^8
     */
    public void prepararTablas() {
        int i, j;

        expTable[0] = 1;
        for (i = 1; i < 255; i++) {
            j = (expTable[i - 1] << 1) ^ expTable[i - 1];//Corrimiento de 1 byte mas un xor con el dato original
            if ((j & 0x100) != 0) {//256 binario
                j ^= raizGenerador;
            }
            expTable[i] = j;
        }
        for (i = 1; i < 255; i++) {
            logTable[expTable[i]] = i;
        }
    }

    /**
     * Método que determina el numero de rondas dependiendo del tamaño de la
     * clave inicial
     *
     * @param tamClave tamaño de la clave otorgada
     * @return numero de subrondas que se deben aplicar en el des/cifrado
     * cifradorAES
     */
    public static int getNumRondas(int tamClave) {
        switch (tamClave) {
            case 16:    // 16 byte = 128 bit key
                return 10;
            case 24:    // 24 byte = 192 bit key
                return 12;
            default:    // 32 byte = 256 bit key
                return 14;
        }
    }

    /**
     * Método que multiplica dos elementos del campo de galois en 2^8 Se usan
     * las tablas de logaritmos para acelerar el procedimiento,
     *
     * @param a primer valor
     * @param b segundo
     * @return producto entre los valores con su polinomio generador
     */
    public  int galoisMultiplicacion(int a, int b) {
        return (a != 0 && b != 0)
                ? expTable[(logTable[a & 0xFF] + logTable[b & 0xFF]) % 255]
                : 0;
        // Un numero multiplicado por 0 es 0.
    }

    //......................................................................
    /**
     * Encriptacion de cifradorAES con 128 bits usando una clave previamente
     * establecida
     *
     * @param claro Bloque de texto Claro transformado en bytes para encriptarse
     * @return el arreglo de bytes del texto cifrado
     */
    public byte[] encriptar(byte[] claro) {

        byte[] estado = new byte[tamBloque];    // Estado del bloque de texto
        byte[] tempEstado = new byte[tamBloque];    // Estado temporal del bloque
        byte[] claveEncript;                //clave de encriptacion de la ronda actual 
        int  fila, col;

        // revisar que no hayan errores en el texto a cifrar
        if (claro == null) {
            throw new IllegalArgumentException("Campo de texto vacío");
        }
        if (claro.length != tamBloque) {
            throw new IllegalArgumentException("Longitud de texto erronea/No llenada");
        }

        //Se toma la primera columna de la primera clave de para hacer un AddRoundKey Inicial antes de las subrondas
        claveEncript = llaveEncriptar[0];
        int i,k;
        for (i = 0; i < tamBloque; i++) {
            estado[i] = (byte) (claro[i] ^ claveEncript[i]);
        }

        // for each round except last, apply round transforms
        //
        for (int rounds = 1; rounds < numRondas; rounds++) {
            claveEncript = llaveEncriptar[rounds];            // Se obtiene la clave de la subronda

            // SubBytes sustituyendo con la S-Box de encriptacion 
            for (i = 0; i < tamBloque; i++) {
                tempEstado[i] = SBox[estado[i] & 0xFF];
            }

            // ShiftRows en cada fila segun lo definido en filasShift {0,1,2,3}
            for (i = 0; i < tamBloque; i++) {
                fila = i % numFilas;
                k = (i + (filasShift[fila] * numFilas)) % tamBloque;
                estado[i] = tempEstado[k];
            }

            // MixColumnsal estado temporal
            for (col = 0; col < numCols; col++) {
                i = col * numFilas;
                tempEstado[i] = (byte) (galoisMultiplicacion(2, estado[i]) ^ galoisMultiplicacion(3, estado[i + 1]) ^ estado[i + 2] ^ estado[i + 3]);
                tempEstado[i + 1] = (byte) (estado[i] ^ galoisMultiplicacion(2, estado[i + 1]) ^ galoisMultiplicacion(3, estado[i + 2]) ^ estado[i + 3]);
                tempEstado[i + 2] = (byte) (estado[i] ^ estado[i + 1] ^ galoisMultiplicacion(2, estado[i + 2]) ^ galoisMultiplicacion(3, estado[i + 3]));
                tempEstado[i + 3] = (byte) (galoisMultiplicacion(3, estado[i]) ^ estado[i + 1] ^ estado[i + 2] ^ galoisMultiplicacion(2, estado[i + 3]));
            }

            // AddRoundKey con la clave de la ronda actual
            for (i = 0; i < tamBloque; i++) {
                estado[i] = (byte) (tempEstado[i] ^ claveEncript[i]);
            }

        }

        // La ultima ronda solo tiene SubBytes, ShiftRows and AddRoundKey, no hace mixcolumns
        claveEncript = llaveEncriptar[numRondas];            //Obtener ultima clave expandida

        // SubBytes al estado
        for (i = 0; i < tamBloque; i++) {
            estado[i] = SBox[estado[i] & 0xFF];
        }

        // ShiftRows al estado temporal
        for (i = 0; i < tamBloque; i++) {
            fila = i % numFilas;
            k = (i + (filasShift[fila] * numFilas)) % tamBloque;
            tempEstado[i] = estado[k];
        }

        // AddRoundKey al estado final
        for (i = 0; i < tamBloque; i++) {
            estado[i] = (byte) (tempEstado[i] ^ claveEncript[i]);
        }
        //Ya se ha cambiado el estado con todas las rondas del encriptamiento del cifradorAES
        return (estado);
    }

    //......................................................................
    /**
     * cifradorAES desencriptar 128-bit ciphertext using key previously set.
     * Desencripción de cifradorAES en 128 bits usando una llave previamente
     * establecida
     *
     * @param cifrado texto en 128 bits cifrado
     * @return el texto descifrado
     */
    public byte[] desencriptar(byte[] cifrado) {

        byte[] estado = new byte[tamBloque];    // Bloque de estado Actual
        byte[] tempEstado = new byte[tamBloque];    // Bloque temporal del estado
        byte[] claveDesencript;                // llave de desencripcion para cada ronda
        int i, k, fila, colm;

        // revisar que la composición no sea erronea
        if (cifrado == null) {
            throw new IllegalArgumentException("texto cifrado vacio");
        }
        if (cifrado.length != tamBloque) {
            throw new IllegalArgumentException("tamaño del texto cifrado no correcto");
        }

        // AddRoundKey Inicial y se tiene tambien la llave de desencripcion inicial
        claveDesencript = llaveDesencriptar[0];
        for (i = 0; i < tamBloque; i++) {
            estado[i] = (byte) (cifrado[i] ^ claveDesencript[i]);
        }

        // Las siguientes 9 rondas
        for (int r = 1; r < numRondas; r++) {
            claveDesencript = llaveDesencriptar[r];            // Obtener la subclave de la ronda

            // ShiftRows invertido, en vez de sumar se resta el indice.
            for (i = 0; i < tamBloque; i++) {
                fila = i % numFilas;
                k = (i + tamBloque - (filasShift[fila] * numFilas)) % tamBloque;
                tempEstado[i] = estado[k];
            }

            // SubBytes inverso usando la S-Box para desencriptar (SdBox)
            for (i = 0; i < tamBloque; i++) {
                estado[i] = SdBox[tempEstado[i] & 0xFF];
            }

            // Aplicar AddRoundKey con la llave de ronda de desencripción
            for (i = 0; i < tamBloque; i++) {
                tempEstado[i] = (byte) (estado[i] ^ claveDesencript[i]);
            }

            // MixColumns inverso con los campos de galois
            for (colm = 0; colm < numCols; colm++) {
                i = colm * numFilas;        // start index for this col
                estado[i] = (byte) (galoisMultiplicacion(14, tempEstado[i]) ^ galoisMultiplicacion(11, tempEstado[i + 1]) ^ galoisMultiplicacion(13, tempEstado[i + 2]) ^ galoisMultiplicacion(9, tempEstado[i + 3]));
                estado[i + 1] = (byte) (galoisMultiplicacion(9, tempEstado[i]) ^ galoisMultiplicacion(14, tempEstado[i + 1]) ^ galoisMultiplicacion(11, tempEstado[i + 2]) ^ galoisMultiplicacion(13, tempEstado[i + 3]));
                estado[i + 2] = (byte) (galoisMultiplicacion(13, tempEstado[i]) ^ galoisMultiplicacion(9, tempEstado[i + 1]) ^ galoisMultiplicacion(14, tempEstado[i + 2]) ^ galoisMultiplicacion(11, tempEstado[i + 3]));
                estado[i + 3] = (byte) (galoisMultiplicacion(11, tempEstado[i]) ^ galoisMultiplicacion(13, tempEstado[i + 1]) ^ galoisMultiplicacion(9, tempEstado[i + 2]) ^ galoisMultiplicacion(14, tempEstado[i + 3]));
            }

        }

        //Ultima ronda donde no se hace mix Columns, se carga la llave de desencripción final
        claveDesencript = llaveDesencriptar[numRondas];

        // ShiftRows Inverso
        for (i = 0; i < tamBloque; i++) {
            fila = i % numFilas;
            k = (i + tamBloque - (filasShift[fila] * numFilas)) % tamBloque;
            tempEstado[i] = estado[k];
        }

        // SubBytes Inverso usando la SBox de desencripción
        for (i = 0; i < tamBloque; i++) {
            tempEstado[i] = SdBox[tempEstado[i] & 0xFF];
        }

        // AddRoundKey con la ultima llave y sobreescritura al estado final
        for (i = 0; i < tamBloque; i++) {
            estado[i] = (byte) (tempEstado[i] ^ claveDesencript[i]);
        }

        return (estado);
    }

    //......................................................................
    /**
     * Creación de la llave de sesión del usuario con una llave dada por este..
     *
     * @param clavek La llave de cifrado a usar por cifradorAES, de
     * 128/192/256-bit // 16/24/32 Caracteres-Bytes
     */
    public void setClave(byte[] clavek) {

        int bloque = tamBloque / 4;//Se fragmenta el bloque en 4 columnas
        int tamClave = clavek.length; //Tamaño de la llave
        int N = tamClave / 4; // numero de columnas de cada bloque de claves 
        

        // Revisar que no hayan errores en la composición de la clave
        if (clavek == null) {
            throw new IllegalArgumentException("Llave vacia");
        }
        if (!(clavek.length == 16 || clavek.length == 24 || clavek.length == 32)) {
            throw new IllegalArgumentException("Tamaño de llave incorrecto, debe ser de 16, 24 o 32 caracteres");
        }

        // Se establecen el número de rondas con respecto a la longitud de la llave.
        numRondas = getNumRondas(tamClave);
        int cuentaRondas = (numRondas + 1) * bloque;//Determina el numero de espacios que van a haber al realizar la expansion
        int i, j, r;
        //Cada fila mantiene los valores de la clave expandida
        byte[] fila0 = new byte[cuentaRondas];
        byte[] fila1 = new byte[cuentaRondas];
        byte[] fila2 = new byte[cuentaRondas];
        byte[] fila3 = new byte[cuentaRondas];

        // Reservar la memoria para las claves de sesion, a nivel de bytes, con n+1 rondas y con el tamaño del bloque
        llaveEncriptar = new byte[numRondas + 1][tamBloque]; // la clave de encriptación para cada ronda
        llaveDesencriptar = new byte[numRondas + 1][tamBloque]; // la clave de desencritpación para cada ronda

        // Las primeras N columnas (El primer bloque de clave) se llenan con la clave de cifrado inicial dada
        for (i = 0, j = 0; i < N; i++) {
            fila0[i] = clavek[j++];
            fila1[i] = clavek[j++];
            fila2[i] = clavek[j++];
            fila3[i] = clavek[j++];
            //System.out.println(w0[i] + " " +w1[i]+" "+w2[i]+" "+w3[i]);

        }

        //Expansion de la clave de cifrado para las subclaves de las sigientes rondas
        byte t0, t1, t2, t3, viejo0;        // Bytes temporales por cada columna que se esté llenando.

        for (i = N; i < cuentaRondas; i++) {
            t0 = fila0[i - 1];
            t1 = fila1[i - 1];
            t2 = fila2[i - 1];
            t3 = fila3[i - 1];    // Se guardan los bytes temporales generar recalculos a las columnas multiplos de 4
            if (i % N == 0) {//Las columnas múltiplo de 4 se deben recalcular usando RotWord y SubBytes con la columna anterior

                //Se hace la rotacion, luego se busca en la caja SBox de sustitución de bytes el valor correspondiente
                // 
                viejo0 = t0;            // se guarda le primer valor t0 para aplicar la rotación

                //Se le aplica & lógico con 255 para que se obtenga el valor positivo del decimal
                //De esta manera pese a que haya valores negativos, esto los volvería positivo
                //Evita errores de indexación en la caja SBox de sustitución de bytes
                t0 = (byte) (SBox[t1 & 0xFF] ^ rcon[i / N]);    //La matriz rcon solo tiene un número en su primera posición
                t1 = (byte) (SBox[t2 & 0xFF]);                 //Para las siguientes, aplicar XOR no haría ningun cambio
                t2 = (byte) (SBox[t3 & 0xFF]);
                t3 = (byte) (SBox[viejo0 & 0xFF]);

            }

            //Se aplica el XOR con la columna correspondiente, el Nk sirve para indexar la columna que hay que aplicar
            fila0[i] = (byte) (fila0[i - N] ^ t0);
            fila1[i] = (byte) (fila1[i - N] ^ t1);
            fila2[i] = (byte) (fila2[i - N] ^ t2);
            fila3[i] = (byte) (fila3[i - N] ^ t3);
        }

        //Se guardan ahora las llaves de encriptación y desencriptación con los calculos realizados por ronda y fila 
        for (r = 0, i = 0; r < numRondas + 1; r++) {    // por cada ronda
            for (j = 0; j < bloque; j++) {        // por cada fila de la ronda
                llaveEncriptar[r][4 * j] = fila0[i];
                llaveEncriptar[r][4 * j + 1] = fila1[i];
                llaveEncriptar[r][4 * j + 2] = fila2[i];
                llaveEncriptar[r][4 * j + 3] = fila3[i];

                //La llave de encriptación va en reversa a la de encriptación
                llaveDesencriptar[numRondas - r][4 * j] = fila0[i];
                llaveDesencriptar[numRondas - r][4 * j + 1] = fila1[i];
                llaveDesencriptar[numRondas - r][4 * j + 2] = fila2[i];
                llaveDesencriptar[numRondas - r][4 * j + 3] = fila3[i];
                i++;
            }
        }

    }

    /**
     *
     * @param arreglo Bytes que contienen un string
     * @return Cadena de caracteres resultante
     */
    public static String bytesAString(byte[] arreglo) {
        String res = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arreglo.length; i++) {
            int n = (int) arreglo[i];
            if (n < 0) {
                n += 256;
            }
            sb.append((char) n);
        }
        res = sb.toString();
        return res;
    }

    /**
     *
     * @param palabra
     * @return
     */
    public static byte[] stringABytes(String palabra) {
        byte[] temp = new byte[palabra.length()];
        for (int i = 0; i < palabra.length(); i++) {
            temp[i] = (byte) palabra.charAt(i);
        }
        return temp;
    }

    /**
     *
     * @param t
     * @return
     */
    public static String static_intArrayToString(int[] t) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < t.length; i++) {
            sb.append((char) t[i]);
        }
        return sb.toString();
    }

    /**
     * Metodo que recibe los datos y el modo (encriptar o desencriptar) Se
     * transforma
     *
     * @param palabra
     * @param modo
     * @return
     */
    public String aesCriptacion(String palabra, int modo) {
        cifradorAES aes = this;
        if (palabra.length() / 16 > ((int) palabra.length() / 16)) {//Se le aplica el relleno al bloque en caso que haga falta
            int residuo = palabra.length() - ((int) palabra.length() / 16) * 16;
            for (int i = 0; i < residuo; i++) {
                palabra += " ";
            }
        }
        int numPartes = (int) palabra.length() / 16;//Numero de partes en las que se fragmenta la cadena
        byte[] res = new byte[palabra.length()];
        String partePalabra = "";
        byte[] parteByte = new byte[16];
        for (int p = 0; p < numPartes; p++) {
            partePalabra = palabra.substring(p * 16, p * 16 + 16);//Se fragmentan cada 16 caracteres
            parteByte = stringABytes(partePalabra);//Se vuelve esta cadena en un bloque de bytes para encriptar o desencriptar
            if (modo == 1) {//Encriptar la cadena

                parteByte = aes.encriptar(parteByte);
            }
            if (modo == 2) {//Desencriptar el cifrado
                parteByte = aes.desencriptar(parteByte);
            }
            for (int b = 0; b < 16; b++) {
                res[p * 16 + b] = parteByte[b];//Una vez el bloque ha sido operado, se va pasando al arreglo de bytes a string nuevamente
            }
        }
        return bytesAString(res);
    }

    /**
     *
     * @param palabra a encriptar
     * @return
     */
    public String encriptar(String palabra) {
        while ((palabra.length() % 32) != 0) {
            palabra += " ";//Se le aplica el relleno si la palabra no es divisible en un bloque de 32
        }
        return aesCriptacion(palabra, 1);
    }

    /**
     *
     * @param cifrado
     * @return palabra ya descifrada
     */
    public String desencriptar(String cifrado) {
        return aesCriptacion(cifrado, 2).trim();//Quita los espacios en blanco
    }

    /**
     *
     * @param llave
     */
    public void setClave(String llave) {
        //System.out.println("La llave inicial es: "+key);
        setClave(stringABytes(llave));
    }
}
