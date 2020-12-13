package com.andorid.basedatos.Modelo.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.andorid.basedatos.Modelo.Student;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface StudentDao {
    @Query("DELETE  FROM Student")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<List<Long>> insert(List<Student> entity);

    @Query("Select * from Student")
    Maybe<List<Student>> getStudentForCourse();
}
