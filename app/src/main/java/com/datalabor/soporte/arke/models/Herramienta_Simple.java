package com.datalabor.soporte.arke.models;
import java.io.Serializable;


public class Herramienta_Simple implements Serializable{

    private int _id;
    private String _desc;
    private String _imagelink;
    private String _clave;

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

    public String get_imagelink() {
        return _imagelink;
    }

    public void set_imagelink(String _imagelink) {
        this._imagelink = _imagelink;
    }

    public String get_clave() {
        return _clave;
    }

    public void set_clave(String _clave) {
        this._clave = _clave;
    }

    public String get_ubicacion() {
        return _ubicacion;
    }

    public void set_ubicacion(String _ubicacion) {
        this._ubicacion = _ubicacion;
    }

    private String _ubicacion;



}
