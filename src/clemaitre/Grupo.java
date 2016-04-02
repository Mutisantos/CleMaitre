/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clemaitre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @autor Petta
 */
public class Grupo implements Serializable{
    
    private String nombre;
    private List<Entrada> entradas = new ArrayList<Entrada> ();
    private List<Grupo> subgrupo = new ArrayList<Grupo> ();
    
    
    public Grupo(){
        
    }
    
    public Grupo(Grupo A){
        this.nombre = A.getNombre();
        for (Entrada E: A.getEntradas()){
            Entrada EE = new Entrada(E);
            this.entradas.add(EE);
        }
        for(Grupo G : A.getSubgrupo()){
            Grupo GG = new Grupo(G);
            this.subgrupo.add(GG);
        }
        
    }
    
    public Grupo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }

    public List<Grupo> getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(List<Grupo> subgrupo) {
        this.subgrupo = subgrupo;
    }
    
    public void addEntrada(Entrada entrada) {
        this.entradas.add(entrada);
    }
    
    public void deleteEntrada(String entrada) {
        for (int i = 0; i < entradas.size(); i++) {
            
            if (entradas.get(i).getNombreentrada().equals(entrada)){
                
                entradas.remove(i);
 
            }
     
        }
    }
    
    

   public void addSubGrupo(Grupo p) {
        subgrupo.add(p);
    }
    @Override
    public String toString() {
        return "Grupo: "+ nombre ;
    }
    

}
