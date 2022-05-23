package com.example.firestore.models;

public class PartidosModel {
    String fecha;
    String id;
    String lugar;
    String oponente;
    String rival;
    String hora;

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public PartidosModel(String fecha, String id, String lugar, String oponente, String rival, String hora) {
        this.fecha = fecha;
        this.id = id;
        this.lugar = lugar;
        this.oponente = oponente;
        this.rival = rival;
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getOponente() {
        return oponente;
    }

    public void setOponente(String oponente) {
        this.oponente = oponente;
    }

    public String getRival() {
        return rival;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }
}
