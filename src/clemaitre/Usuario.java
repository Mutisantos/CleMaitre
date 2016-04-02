package clemaitre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @autor Petta
 */
public class Usuario implements Serializable {

    private String nombre;
    private String contrasenia;
    private List<Grupo> grupos = new ArrayList<Grupo>();

    public Usuario(String nombre, String contrasenia) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }

    public Usuario(Usuario copia) {
        this.nombre = copia.getNombre();
        this.contrasenia = copia.getContrasenia();
        for(Grupo G: copia.getGrupos()){
            Grupo GG = new Grupo(G);
            this.grupos.add(GG);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public void addGrupo(Grupo grupo) {
        this.grupos.add(grupo);
    }

    public void deleteGrupo(String nombre) {

        for (int i = 0; i < grupos.size(); i++) {

            if (grupos.get(i).getNombre().equals(nombre)) {

                grupos.remove(i);
            }

        }

    }

    public void deleteSubGrupo(String nombre) {

        for (Grupo grupo : grupos) {

            for (int i = 0; i < grupo.getSubgrupo().size(); i++) {

                if (grupo.getSubgrupo().get(i).getNombre().equals(nombre)) {

                    grupo.getSubgrupo().remove(i);
                }

            }

        }

    }

    @Override
    public String toString() {
        return "Usuario: " + nombre + ", Contraseña: " + contrasenia + " ";
    }

}
