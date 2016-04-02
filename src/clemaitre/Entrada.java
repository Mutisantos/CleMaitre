/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clemaitre;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @autor Petta
 */
public class Entrada implements Serializable {

    private String nombreentrada;
    private String usuario;
    private String clave;
    private String confirmacionclave;
    private String URL;
    private String nota;
    private Calendar fechacreacion;
    private Calendar fechamodificacion;
    private Calendar fechaultimoacceso;
    private Calendar fechaexpira;

    public Entrada(String nombreentrada, String usuario, String clave, String confirmacionclave, String URL, String nota, Calendar fechacreacion, Calendar fechamodificacion, Calendar fechaultimoacceso, Calendar fechaexpira) {
        this.nombreentrada = nombreentrada;
        this.usuario = usuario;
        this.clave = clave;
        this.confirmacionclave = confirmacionclave;
        this.URL = URL;
        this.nota = nota;
        this.fechacreacion = fechacreacion;
        this.fechamodificacion = fechamodificacion;
        this.fechaultimoacceso = fechaultimoacceso;
        this.fechaexpira = fechaexpira;
    }

    public Entrada() {

    }

    public Entrada(Entrada EE) {
        this.nombreentrada = EE.getNombreentrada();
        this.usuario = EE.getUsuario();
        this.clave = EE.getClave();
        this.confirmacionclave = EE.getConfirmacionclave();
        this.URL = EE.getURL();
        this.nota = EE.getNota();
        this.fechacreacion = EE.getFechacreacion();
        this.fechamodificacion = EE.getFechamodificacion();
        this.fechaultimoacceso = EE.getFechaultimoacceso();
        this.fechaexpira = EE.getFechaexpira();
    }

    public String getNombreentrada() {
        return nombreentrada;
    }

    public void setNombreentrada(String nombreentrada) {
        this.nombreentrada = nombreentrada;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getConfirmacionclave() {
        return confirmacionclave;
    }

    public void setConfirmacionclave(String confirmacionclave) {
        this.confirmacionclave = confirmacionclave;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Calendar getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Calendar fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public void setFechacreacion(String fecha) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            cal.setTime(sdf.parse(fecha));
        } catch (ParseException ex) {
            Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Calendar getFechamodificacion() {
        return fechamodificacion;
    }

    public void setFechamodificacion(Calendar fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }

    public void setFechamodificacion(String fecha) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            cal.setTime(sdf.parse(fecha));
        } catch (ParseException ex) {
            Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Calendar getFechaultimoacceso() {
        return fechaultimoacceso;
    }

    public void setFechaultimoacceso(Calendar fechaultimoacceso) {
        this.fechaultimoacceso = fechaultimoacceso;
    }

    public void setFechaultimoacceso(String fecha) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            cal.setTime(sdf.parse(fecha));
        } catch (ParseException ex) {
            Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Calendar getFechaexpira() {
        return fechaexpira;
    }

    public void setFechaexpira(Calendar fechaexpira) {
        this.fechaexpira = fechaexpira;
    }

    public void setFechaexpira(String fecha) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            cal.setTime(sdf.parse(fecha));
        } catch (ParseException ex) {
            Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String formatoFecha(Calendar fecha) {

        return (fecha.get(Calendar.DAY_OF_MONTH) + "/" + fecha.get(Calendar.MONTH) + "/" + fecha.get(Calendar.YEAR) + " " + fecha.get(Calendar.HOUR) + ":" + fecha.get(Calendar.MINUTE) + ":" + fecha.get(Calendar.SECOND));

    }

    @Override
    public String toString() {
        return "Titulo: " + nombreentrada + ", Nombre Usuario: " + usuario + ", Clave: ********, URL: " + URL + ", Fecha Creación: " + formatoFecha(fechacreacion) + ", Ultima Modificación: " + formatoFecha(fechamodificacion) + ", Ultimo Acceso: " + formatoFecha(fechaultimoacceso) + ", Expira: " + formatoFecha(fechaexpira) + " ";
    }

}
