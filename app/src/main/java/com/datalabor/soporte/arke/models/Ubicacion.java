package com.datalabor.soporte.arke.models;
import java.io.Serializable;


public class Ubicacion implements Serializable{

    private int _id;
    private int _num;
    private String _ubicacion;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public String get_ubicacion() {
        return _ubicacion;
    }

    public void set_ubicacion(String _ubicacion) {
        this._ubicacion = _ubicacion;
    }

    public String get_responsable() {
        return _responsable;
    }

    public void set_responsable(String _responsable) {
        this._responsable = _responsable;
    }

    private String _responsable;

    public Ubicacion()
    {

    }


}
