package com.datalabor.soporte.arke.models;
import java.io.Serializable;
import java.util.Date;


public class Mantenimiento  implements Serializable{

    private int _id;
    private String _desc;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    public Date get_fecha() {
        return _fecha;
    }

    public void set_fecha(Date _fecha) {
        this._fecha = _fecha;
    }

    private Date _fecha;

    public Mantenimiento()
    {
        _fecha = new Date();
    }



}
