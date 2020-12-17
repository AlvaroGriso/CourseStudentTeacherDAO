package com.andorid.basedatos.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.andorid.basedatos.Modelo.Teacher;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface TeacherDao {
    @Query("DELETE  FROM Teacher")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<List<Long>> insert(List<Teacher> entity);

    @Query("Select * from Teacher where asignatura = :course")
    Maybe<Teacher> getTeacherForCourse(String course);
}
