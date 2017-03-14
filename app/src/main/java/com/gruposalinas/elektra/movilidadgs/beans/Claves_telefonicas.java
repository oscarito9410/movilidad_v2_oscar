package com.gruposalinas.elektra.movilidadgs.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yvegav on 21/12/2016.
 */

public class Claves_telefonicas extends BaseBean implements Serializable
{
    String numero_empleado;
    String codigo;
    String id;
    String pais;
    ArrayList<Claves_telefonicas> claves_telefonicasArrayList;

    public ArrayList<Claves_telefonicas> getClaves_telefonicasArrayList() {
        return claves_telefonicasArrayList;
    }

    public void setClaves_telefonicasArrayList(ArrayList<Claves_telefonicas> claves_telefonicasArrayList) {
        this.claves_telefonicasArrayList = claves_telefonicasArrayList;
    }

    public String getNumero_empleado() {
        return numero_empleado;
    }

    public void setNumero_empleado(String numero_empleado) {
        this.numero_empleado = numero_empleado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
