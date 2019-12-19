package com.example.knkapp.Models;

public class ModelChat {
    String tinnhan, nhantin, guitin, thoigian;
    boolean daxem;

    public ModelChat() {

    }

    public ModelChat(String tinnhan, String nhantin, String guitin, String thoigian, boolean daxem) {
        this.tinnhan = tinnhan;
        this.nhantin = nhantin;
        this.guitin = guitin;
        this.thoigian = thoigian;
        this.daxem = daxem;
    }

    public String getTinnhan() {
        return tinnhan;
    }

    public void setTinnhan(String tinnhan) {
        this.tinnhan = tinnhan;
    }

    public String getNhantin() {
        return nhantin;
    }

    public void setNhantin(String nhantin) {
        this.nhantin = nhantin;
    }

    public String getGuitin() {
        return guitin;
    }

    public void setGuitin(String guitin) {
        this.guitin = guitin;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public boolean isDaxem() {
        return daxem;
    }

    public void setDaxem(boolean daxem) {
        this.daxem = daxem;
    }
}
