package com.datalabor.soporte.arke.models;

public class Responsable {
    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    private String _name;
    private String _desc;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    private Integer _id;

    public Responsable()
    {
        _name = "";
        _desc = "";
    }

}
