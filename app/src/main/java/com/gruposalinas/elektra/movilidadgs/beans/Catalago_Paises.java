package com.gruposalinas.elektra.movilidadgs.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by yvegav on 23/12/2016.
 */

@DatabaseTable(tableName = "catalogo_paises")
public class Catalago_Paises extends BaseBean implements Serializable
{
    public static final String ID 		= "id";
    public static final String CODIGO 	= "codigo";
    public static final String PAIS 	= "pais";



    @DatabaseField(columnName = ID)
    private String Id;
    @DatabaseField(columnName = CODIGO)
    private String Codigo;
    @DatabaseField(columnName = PAIS)
    private String Pais;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }
}
