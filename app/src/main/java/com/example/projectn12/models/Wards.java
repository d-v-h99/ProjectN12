package com.example.projectn12.models;

public class Wards {
    private int code;
    private String codename;
    private int district_code;
    private String division_type;
    private String name;

    public Wards() {
    }

    public Wards(int code, String codename, int district_code, String division_type, String name) {
        this.code = code;
        this.codename = codename;
        this.district_code = district_code;
        this.division_type = division_type;
        this.name = name;
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

    public int getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(int district_code) {
        this.district_code = district_code;
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
}