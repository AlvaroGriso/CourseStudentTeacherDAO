package com.andorid.basedatos.Modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Teacher {
    @PrimaryKey
    private int codigo;
    private String nombre;
    private String apellido;
    private String asignatura;

    public Teacher(int codigo, String nombre, String apellido, String asignatura) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.asignatura = asignatura;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "codigo=" + codigo +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", asignatura='" + asignatura + '\'' +
                '}';
    }
}
