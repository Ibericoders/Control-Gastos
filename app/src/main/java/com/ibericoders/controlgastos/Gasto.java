package com.ibericoders.controlgastos;

/**
 * Created by Jorge R on 04/06/2017.
 */

public class Gasto {

    private String nombre;
    private String descripcion;
    private double cantidad;
    private String fecha;

    public Gasto(String nombre, String descripcion, double cantidad, String fecha) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return nombre+"|"+descripcion+"|"+cantidad+"|"+fecha;
    }
}
