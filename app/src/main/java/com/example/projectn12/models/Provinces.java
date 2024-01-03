package com.example.projectn12.models;

import java.util.List;

public class Provinces {
    private String code;
    private String codename;
    private List<Districts> districts;
    private String division_type;
    private String name;
    private int phone_code;

    public Provinces() {
    }

    public Provinces(String code, String codename, List<Districts> districts, String division_type, String name, int phone_code) {
        this.code = code;
        this.codename = codename;
        this.districts = districts;
        this.division_type = division_type;
        this.name = name;
        this.phone_code = phone_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public List<Districts> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Districts> districts) {
        this.districts = districts;
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

    public int getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(int phone_code) {
        this.phone_code = phone_code;
    }
}