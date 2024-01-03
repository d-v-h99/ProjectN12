package com.example.projectn12.models;

import java.util.List;

public class Districts {
    private int code;
    private String codename;
    private String division_type;
    private String name;
    private int province_code;
    private List<Wards> wards;

    public Districts() {
    }

    public Districts(int code, String codename, String division_type, String name, int province_code, List<Wards> wards) {
        this.code = code;
        this.codename = codename;
        this.division_type = division_type;
        this.name = name;
        this.province_code = province_code;
        this.wards = wards;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getDivision_type() {
        return division_type;
    }

    public void setDivision_type(String division_type) {
        this.division_type = division_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProvince_code() {
        return province_code;
    }

    public void setProvince_code(int province_code) {
        this.province_code = province_code;
    }

    public List<Wards> getWards() {
        return wards;
    }

    public void setWards(List<Wards> wards) {
        this.wards = wards;
    }
}