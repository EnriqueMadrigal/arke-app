package com.datalabor.soporte.arke.models;
import java.io.Serializable;


public class Catalogo implements Serializable {

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

    public String get_imagelink() {
        return _imagelink;
    }

    public void set_imagelink(String _imagelink) {
        this._imagelink = _imagelink;
    }

    private String _imagelink;

    public Catalogo()
    {

    }



}
