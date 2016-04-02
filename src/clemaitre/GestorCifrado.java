package clemaitre;

/**
 * Clase que carga los archivos, los guarda y calcula sus hash
 *
 * @author Esteban Hernández y Camilo Benavides
 */
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.*;
import java.util.ArrayList;

/**
 *
 * @author Hernandez y Benavides
 */
public class GestorCifrado {

    /**
     *
     */
    private String nombreArchivo;

    /**
     *
     */
    private String hashArchivo;

    /**
     *
     */
    private String Contenido;

    /**
     *
     */
    private cifradorAES cifrador;

    /**
     *
     */
    public GestorCifrado() {
        this.cifrador = new cifradorAES();
        this.cifrador.setClave("RIJNDAELcodigo01");

    }

    /**
     * Guardar el archivo con el hash de cada otro archivo.
     *
     * @param usuario
     * @param archivo
     */
    public void escribirHash(String usuario, String archivo) {
        PrintWriter salida;
        PrintWriter flujoSalida = null;
        PrintWriter dS = null;
        try {
            Process p = Runtime.getRuntime().exec("attrib -h " + "Hasher" + usuario + ".txt");//Mostrar el archivo con los atributos del sistema
            p.waitFor();
            salida = new PrintWriter(new FileWriter(new File("Hasher" + usuario + ".txt")));
            String hash = this.hashFile(archivo);//Guardo el Hash del archivo de las contraseñas
            flujoSalida = new PrintWriter(salida);
            dS = new PrintWriter(flujoSalida);
            dS.println(cifrador.encriptar(hash));
            salida.close();
            Runtime.getRuntime().exec("attrib +h " + "Hasher" + usuario + ".txt");//Ocultar el archivo con los atributos del sistema

        } catch (IOException ex) {
            Logger.getLogger(GestorCifrado.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GestorCifrado.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GestorCifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        flujoSalida.close();
        dS.close();
    }

    /**
     * Método que obtiene el archivo del hash de un usuario para evaluar la
     * integridad de este archivo. El archivo de hash se mantiene oculto hasta
     * que se ejecuta este método.
     *
     * @param usuario Nombre del usuario para calcular
     * @return Hash original del archivo
     */
    public String obtenerHash(String usuario) {
        String Hash = new String();
        String todo = new String();
        Hash = "";
        try {

            BufferedReader input;
            Runtime.getRuntime().exec("attrib -h " + "Hasher" + usuario + ".txt");//Ocultar el archivo con los atributos del sistema
            input = new BufferedReader(new FileReader(new File("Hasher" + usuario + ".txt")));
            BufferedReader lectura = new BufferedReader(input);
            todo = lectura.readLine();


            Hash = cifrador.desencriptar(todo);           
            while(Hash.length()<32){
                todo += "\n";
                todo += lectura.readLine();
                System.out.println("Entro");
                Hash = cifrador.desencriptar(todo);
            }
            lectura.close();
            Runtime.getRuntime().exec("attrib +h " + "Hasher" + usuario + ".txt");//Ocultar el archivo con los atributos del sistema

        } catch (IOException ex) {
            Logger.getLogger(GestorCifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Hash;
    }

    /**
     * Escribir el archivo de contraseñas del usuario. # Entrar a un grupo
     * general ? Entrar en un subgrupo $ Generar las entradas
     *
     * @param u Usuario donde se van a escribir los datos.
     */
    public void escribirUsuario(Usuario u) {
        PrintWriter salida;
        PrintWriter flujoSalida = null;
        PrintWriter dS = null;

        try {
            salida = new PrintWriter(new FileWriter(new File("pass" + u.getNombre() + ".txt")));
            flujoSalida = new PrintWriter(salida);
            dS = new PrintWriter(flujoSalida);
            dS.println(this.cifrador.encriptar(u.getNombre()));

            System.out.println(u.getNombre());

            dS.println(this.cifrador.encriptar(u.getContrasenia()));//Primeros campos del archivo del usuario 

            System.out.println(u.getContrasenia());
            String cifrado;
            if (!u.getGrupos().isEmpty()) {//¿El usuario tiene grupos?
                for (Grupo G : u.getGrupos()) {
                    cifrado = this.cifrador.encriptar("#");
                    dS.println(cifrado);
                    cifrado = this.cifrador.encriptar(G.getNombre());
                    dS.println(cifrado);
                    if (!G.getSubgrupo().isEmpty()) {

                        for (Grupo sG : G.getSubgrupo()) {
                            cifrado = this.cifrador.encriptar("?");//Marcar el inicio del Subgrupo
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(sG.getNombre());
                            dS.println(cifrado);
                            System.out.println(sG.getNombre());
                            System.out.println(cifrado);
                            System.out.println(this.cifrador.desencriptar(cifrado));

                            if (!sG.getEntradas().isEmpty()) { //¿Hay entradas dentro del subgrupo?
                                cifrado = this.cifrador.encriptar("$"); //Marcar el inicio de las entradas
                                dS.println(cifrado);

                                for (Entrada E : sG.getEntradas()) { //Ingresar las entradas del subgrupo
                                    cifrado = this.cifrador.encriptar(E.getNombreentrada());
                                    dS.println(cifrado);

                                    cifrado = this.cifrador.encriptar(E.getUsuario());
                                    dS.println(cifrado);

                                    cifrado = this.cifrador.encriptar(E.getURL());
                                    dS.println(cifrado);

                                    cifrado = this.cifrador.encriptar(E.getClave());
                                    dS.println(cifrado);

                                    cifrado = this.cifrador.encriptar(E.getNota());
                                    dS.println(cifrado);

                                    cifrado = this.cifrador.encriptar(E.formatoFecha(E.getFechacreacion()));//Fecha Creación
                                    dS.println(cifrado);

                                    cifrado = this.cifrador.encriptar(E.formatoFecha(E.getFechaexpira()));//Fecha Expiración
                                    dS.println(cifrado);

                                    cifrado = this.cifrador.encriptar(E.formatoFecha(E.getFechamodificacion()));//Fecha Creación
                                    dS.println(cifrado);

                                    cifrado = this.cifrador.encriptar(E.formatoFecha(E.getFechaultimoacceso()));//Fecha Creación
                                    dS.println(cifrado);

                                }

                            }
                            cifrado = this.cifrador.encriptar("?FIN"); //Marcar el final del subgrupo
                            dS.println(cifrado);
                        }

                    }
                    if (!G.getEntradas().isEmpty()) {//¿El grupo tiene entradas?
                        for (Entrada E : G.getEntradas()) { //Ingresar las entradas del subgrupo
                            cifrado = this.cifrador.encriptar("$"); //Marcar el inicio de las entradas
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(E.getNombreentrada());
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(E.getUsuario());
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(E.getURL());
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(E.getClave());
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(E.getNota());
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(E.formatoFecha(E.getFechacreacion()));//Fecha Creación
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(E.formatoFecha(E.getFechaexpira()));//Fecha Expiración
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(E.formatoFecha(E.getFechamodificacion()));//Fecha Modificación
                            dS.println(cifrado);
                            cifrado = this.cifrador.encriptar(E.formatoFecha(E.getFechaultimoacceso()));//Fecha Acceso
                            dS.println(cifrado);
                        }
                        cifrado = this.cifrador.encriptar("$FIN"); //Marcar el final de las entradas del Grupo
                        dS.println(cifrado);
                    }
                    cifrado = this.cifrador.encriptar("#FIN"); //Marcar el final de las entradas del Grupo y del archivo
                    dS.println(cifrado);
                }
                cifrado = this.cifrador.encriptar("FINFIN"); //Marcar el final de las entradas del Grupo y del archivo
                dS.println(cifrado);
                salida.close();
            }
        } catch (Exception e) {
            System.out.println("excepcion inesperada:" + e.getMessage());
        }

        flujoSalida.close();
        dS.close();
        this.escribirHash(u.getNombre(), "pass" + u.getNombre() + ".txt");//Si el archivo ya se creó, armar su respectivo Hash
    }

    /**
     * Escribir el archivo de contraseñas de manera binaria
     *
     * @param a : Usuario a guardar
     *
     *
     */
    public void escribirUsuarioBin(Usuario u) throws IOException {

        Usuario a = new Usuario(u);//Copia para no afectar al original
        String nombreAr = (a.getNombre() + ".pass");//Es una extension que solo este proyecto podra reconocer para serializar y deserializar organizaciones
        ObjectOutputStream archivo = new ObjectOutputStream(new FileOutputStream(nombreAr));
        /////CIFRAR TODO ANTES DE SERIALIZAR/////
        a.setNombre(cifrador.encriptar(a.getNombre()));//Cifrar usuario
        a.setContrasenia(cifrador.encriptar(a.getContrasenia()));//Cifrar contraseña

        for (int i = 0; i < a.getGrupos().size(); i++) {//Cifrar grupos

            a.getGrupos().get(i).setNombre(cifrador.encriptar(a.getGrupos().get(i).getNombre()));
            for (int j = 0; j < a.getGrupos().get(i).getSubgrupo().size(); j++) {//Cifrar subgrupos
                a.getGrupos().get(i).getSubgrupo().get(j).setNombre(cifrador.encriptar(a.getGrupos().get(i).getSubgrupo().get(j).getNombre()));
                for (int k = 0; k < a.getGrupos().get(i).getSubgrupo().get(j).getEntradas().size(); k++) {//Cifrar entradas
                    String cifrado = cifrador.encriptar(a.getGrupos().get(i).getSubgrupo().get(j).getEntradas().get(k).getClave());
                    a.getGrupos().get(i).getSubgrupo().get(j).getEntradas().get(k).setClave(cifrado);
                    cifrado = cifrador.encriptar(a.getGrupos().get(i).getSubgrupo().get(j).getEntradas().get(k).getUsuario());
                    a.getGrupos().get(i).getSubgrupo().get(j).getEntradas().get(k).setUsuario(cifrado);
                }
            }
            for (int k = 0; k < a.getGrupos().get(i).getEntradas().size(); k++) {//Cifrar entradas
                String cifrado = cifrador.encriptar(a.getGrupos().get(i).getEntradas().get(k).getClave());
                a.getGrupos().get(i).getEntradas().get(k).setClave(cifrado);
                cifrado = cifrador.encriptar(a.getGrupos().get(i).getEntradas().get(k).getUsuario());
                a.getGrupos().get(i).getEntradas().get(k).setUsuario(cifrado);
            }

        }

        archivo.writeObject((Usuario) a);
        archivo.close();

        this.escribirHash(u.getNombre(), nombreAr);
        this.hashArchivo = this.obtenerHash(u.getNombre());
    }

    /**
     *
     * @param usuario
     * @param password
     * @param Ruta
     * @return
     */
    public Usuario cargarUsuario(String usuario, String password, String Ruta) {
        String descifra = new String();
        String user = new String();
        String pass = new String();//Verifica que el usuario y contraseña correspondan
        String hash = new String();
        BufferedReader input;
        Usuario ussr = new Usuario(usuario, password);

        try {
            input = new BufferedReader(new FileReader(new File(Ruta)));

            BufferedReader lectura = new BufferedReader(input);
            user = lectura.readLine();
            user = cifrador.desencriptar(user);
            System.out.println(user);
            pass = lectura.readLine();
            pass = cifrador.desencriptar(pass);
            System.out.println(pass);

            if (pass.equals(password) && user.equals(usuario)) {

            } else {
                System.out.println("FALLO DE AUTENTICACIÓN");
                ussr.setNombre("NO AUTENTICADO");
                ussr.setContrasenia("FALLIDO");

            }

            ussr = new Usuario(user, pass);
            Grupo Group = new Grupo();
            Grupo subGrupo = new Grupo();
            Entrada entry = new Entrada();
            ArrayList<Grupo> subGrps = new ArrayList<Grupo>();
            boolean enSubGrupo = false;

            while (!descifra.equals("FINFIN")) {//Entré a los grupos
                descifra = lectura.readLine();
                if (descifra.equals(null)) {
                    break;
                }
                if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                    pass = lectura.readLine();//Lea el segundo fragmento
                    descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                    System.out.println(cifrador.desencriptar(descifra));
                }

                descifra = cifrador.desencriptar(descifra);
                if (descifra.equals("#")) {//Entre a un grupo.
                    descifra = lectura.readLine();

                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas       

                        pass = lectura.readLine();//Lea el segundo fragmento
                        System.out.println(descifra);
                        System.out.println(pass);

                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(descifra);
                        System.out.println(cifrador.desencriptar(descifra));
                    }

                    descifra = cifrador.desencriptar(descifra);
                    Group.setNombre(descifra);
                    System.out.println(descifra + "Grupo");
                }
                if (descifra.equals("?")) {//Enconctré un subgrupo
                    descifra = lectura.readLine();

                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }

                    descifra = cifrador.desencriptar(descifra);
                    subGrupo.setNombre(descifra);
                    System.out.println(descifra + "subgrupo");
                    enSubGrupo = true;
                }
                if (descifra.equals("$")) {
                    descifra = lectura.readLine();
                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }
                    descifra = cifrador.desencriptar(descifra);
                    entry.setNombreentrada(descifra);//Nombre
                    System.out.println(descifra + "entradaNombre");

                    descifra = lectura.readLine();
                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }
                    descifra = cifrador.desencriptar(descifra);
                    entry.setUsuario(descifra);//Usuario

                    descifra = lectura.readLine();
                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }
                    descifra = cifrador.desencriptar(descifra);
                    entry.setURL(descifra);//URL

                    descifra = lectura.readLine();
                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }
                    descifra = cifrador.desencriptar(descifra);
                    entry.setClave(descifra);//Password

                    descifra = lectura.readLine();
                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }
                    descifra = cifrador.desencriptar(descifra);
                    entry.setNota(descifra);//Nota

                    descifra = lectura.readLine();
                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }
                    descifra = cifrador.desencriptar(descifra);
                    entry.setFechacreacion(descifra);//Fecha Creación

                    descifra = lectura.readLine();
                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }
                    descifra = cifrador.desencriptar(descifra);
                    entry.setFechaexpira(descifra);//Fecha Expiración

                    descifra = lectura.readLine();
                    if (descifra.length() != 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }
                    descifra = cifrador.desencriptar(descifra);
                    entry.setFechamodificacion(descifra);//Fecha Mod

                    descifra = lectura.readLine();
                    if (descifra.length() < 32) {//Cuando los saltos de linea parten las entradas                 
                        pass = lectura.readLine();//Lea el segundo fragmento
                        descifra = descifra + "\n" + pass;//Pegar los fragmentos 
                        System.out.println(cifrador.desencriptar(descifra));
                    }
                    descifra = cifrador.desencriptar(descifra);
                    entry.setFechaultimoacceso(descifra);//Fecha Acceso

                    if (enSubGrupo) {
                        subGrupo.addEntrada(entry);
                    } else {
                        Group.addEntrada(entry);
                    }
                }
                if (descifra.equals("?FIN")) {
                    Group.addSubGrupo(subGrupo);
                    enSubGrupo = false;
                }
                if (descifra.equals("#FIN")) {
                    ussr.addGrupo(Group);
                }

            }
            System.out.println("Salida");
            lectura.close();
            input.close();

        } catch (Exception ex) {
            Logger.getLogger(GestorCifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ussr;

    }

    /**
     * Cargar usuario con un archivo binario correspondiente al usuario
     *
     *
     *
     */
    public Usuario cargarUsuarioBin(String ruta) throws IOException, ClassNotFoundException {

        ObjectInputStream archivo1 = new ObjectInputStream(new FileInputStream(ruta));
        Usuario u = (Usuario) archivo1.readObject();
        archivo1.close();
        u.setNombre(cifrador.desencriptar(u.getNombre()));//Cifrar usuario
        System.out.println(u.getNombre());
        u.setContrasenia(cifrador.desencriptar(u.getContrasenia()));//Cifrar contraseña
        System.out.println(u.getContrasenia());
        for (int i = 0; i < u.getGrupos().size(); i++) {//Cifrar grupos

            u.getGrupos().get(i).setNombre(cifrador.desencriptar(u.getGrupos().get(i).getNombre()));
            for (int j = 0; j < u.getGrupos().get(i).getSubgrupo().size(); j++) {//Cifrar subgrupos
                u.getGrupos().get(i).getSubgrupo().get(j).setNombre(cifrador.desencriptar(u.getGrupos().get(i).getSubgrupo().get(j).getNombre()));
                for (int k = 0; k < u.getGrupos().get(i).getSubgrupo().get(j).getEntradas().size(); k++) {//Cifrar entradas
                    String cifrado = cifrador.desencriptar(u.getGrupos().get(i).getSubgrupo().get(j).getEntradas().get(k).getClave());
                    u.getGrupos().get(i).getSubgrupo().get(j).getEntradas().get(k).setClave(cifrado);
                    cifrado = cifrador.desencriptar(u.getGrupos().get(i).getSubgrupo().get(j).getEntradas().get(k).getUsuario());
                    u.getGrupos().get(i).getSubgrupo().get(j).getEntradas().get(k).setUsuario(cifrado);
                }
            }
            for (int k = 0; k < u.getGrupos().get(i).getEntradas().size(); k++) {//Cifrar entradas
                String cifrado = cifrador.desencriptar(u.getGrupos().get(i).getEntradas().get(k).getClave());
                u.getGrupos().get(i).getEntradas().get(k).setClave(cifrado);
                cifrado = cifrador.desencriptar(u.getGrupos().get(i).getEntradas().get(k).getUsuario());
                u.getGrupos().get(i).getEntradas().get(k).setUsuario(cifrado);
            }

        }
        return u;
    }

    /**
     * Calcula el hash de un archivo, dado el nombre y ubicación del mismo
     *
     * @param archivo Ruta+Nombre el archivo
     * @return Hash MD5 del archivo.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public String hashFile(String archivo) throws NoSuchAlgorithmException, IOException {
        System.out.println(archivo);
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream entrada = new FileInputStream(archivo);
        byte[] bytes = new byte[1024];
        int nLectura = 0;
        while ((nLectura = entrada.read(bytes)) != -1) {
            md.update(bytes, 0, nLectura);
        };
        byte[] mdbytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();

    }

}
