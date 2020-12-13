package com.andorid.basedatos.Modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Course {
    @PrimaryKey
    private int idCourse;
    private String course;

    public Course(int idCourse, String course) {
        this.idCourse = idCourse;
        this.course = course;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Course{" +
                "idCourse=" + idCourse +
                ", course='" + course + '\'' +
                '}';
    }
}
