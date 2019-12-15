package com.example.knkapp;

public class ModelBanBe {

// tạo mô hình để chứa data, get và set data

    String name,email,seard,uid;
    public ModelBanBe(){

    }

    public ModelBanBe(String name, String email, String seard, String uid) {
        this.name = name;
        this.email = email;
        this.seard = seard;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSeard() {
        return seard;
    }

    public void setSeard(String seard) {
        this.seard = seard;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
