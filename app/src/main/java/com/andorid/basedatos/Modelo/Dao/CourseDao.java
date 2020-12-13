package com.andorid.basedatos.Modelo.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.andorid.basedatos.Modelo.Course;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface CourseDao {

    @Query("DELETE  FROM Course")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<List<Long>> insert(List<Course> entity);

    @Query("Select * from Course")
    Maybe<List<Course>> getAllCourse();
}
