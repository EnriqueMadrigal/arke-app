package com.datalabor.soporte.arke.models;
import java.io.Serializable;
import java.util.ArrayList;


public class Herramienta implements Serializable{

    private int _id;
    private String _desc;
    private String _imagelink;
    private String _clave;
    private String _modelo;
    private String _marca;

    private String _ubicacion_actual;
    private String _ubicacion_anterior;

    public String get_responsable() {
        return _responsable;
    }

    public void set_responsable(String _responsable) {
        this._responsable = _responsable;
    }

    private String _responsable;

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

    public String get_modelo() {
        return _modelo;
    }

    public void set_modelo(String _modelo) {
        this._modelo = _modelo;
    }

    public String get_marca() {
        return _marca;
    }

    public void set_marca(String _marca) {
        this._marca = _marca;
    }

    public String get_ubicacion_actual() {
        return _ubicacion_actual;
    }

    public void set_ubicacion_actual(String _ubicacion_actual) {
        this._ubicacion_actual = _ubicacion_actual;
    }

    public String get_ubicacion_anterior() {
        return _ubicacion_anterior;
    }

    public void set_ubicacion_anterior(String _ubicacion_anterior) {
        this._ubicacion_anterior = _ubicacion_anterior;
    }

    public String getResponsable() {
        return Responsable;
    }

    public void setResponsable(String responsable) {
        Responsable = responsable;
    }

    public ArrayList<Mantenimiento> get_mantenimientos() {
        return _mantenimientos;
    }

    public void set_mantenimientos(ArrayList<Mantenimiento> _mantenimientos) {
        this._mantenimientos = _mantenimientos;
    }

    private String Responsable;

    private ArrayList<Mantenimiento> _mantenimientos;

    public Herramienta()
    {
        _mantenimientos = new ArrayList<>();
    }


}
