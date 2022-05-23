package com.example.firestore.models;

public class UsersModel {
    String celular;
    String email;
    String cedula;
    String nombres;

    public UsersModel(String celular, String email, String cedula, String nombres) {
        this.celular = celular;
        this.email = email;
        this.cedula = cedula;
        this.nombres = nombres;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}
