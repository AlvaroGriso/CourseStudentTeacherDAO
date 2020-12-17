package com.andorid.basedatos.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andorid.basedatos.Modelo.Course;
import com.andorid.basedatos.Modelo.bbdd.DataBase;
import com.andorid.basedatos.databinding.ActivityCargarDatosBinding;
import com.andorid.basedatos.Modelo.Student;
import com.andorid.basedatos.Modelo.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CargarDatosActivity extends AppCompatActivity {

    String TAG = CargarDatosActivity.class.getSimpleName();

    ActivityCargarDatosBinding binding;
    DataBase dataBase;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCargarDatosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dataBase = DataBase.getDatabase(this);
        obtenerData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "onCreate: ");
                moveToMain();
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables != null) {
            disposables.dispose();
        }
    }

    private CompositeDisposable getDisposables() {
        if (disposables == null)
            disposables = new CompositeDisposable();
        return disposables;
    }

    private void obtenerData() {

        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("datos.json"));
            Log.v(TAG, "obtenerData jsonObject: " + jsonObject);

            JSONArray jsonArrayCourse = jsonObject.getJSONArray("asignaturas");
            Log.v(TAG, "obtenerData jsonArrayCourse: " + jsonArrayCourse);
            if (jsonArrayCourse != null && jsonArrayCourse.length() > 0) {
                //dataBase.courseDao().deleteAll();
                List<Course> courseList = new ArrayList<>();
                for (int i = 0; i < jsonArrayCourse.length(); i++) {
                    String tmp = jsonArrayCourse.getString(i);
                    //Log.v(TAG, "obtenerData tmp: " + tmp);
                    Course misAsignaturas = new Course(i, tmp);
                    courseList.add(misAsignaturas);
                    //Log.v(TAG, "obtenerData courseList: " + courseList);
                }
                saveCourse(courseList);
            }


            JSONArray jsonArrayTeacher = jsonObject.getJSONArray("profesores");
            Log.v(TAG, "obtenerData jsonArrayTeacher: " + jsonArrayTeacher);
            if (jsonArrayTeacher != null && jsonArrayTeacher.length() > 0) {
                //dataBase.teacherDao().deleteAll();
                List<Teacher> teacherList = new ArrayList<>();
                for (int i = 0; i < jsonArrayTeacher.length(); i++) {
                    teacherList.add(new Teacher(
                            jsonArrayTeacher.getJSONObject(i).getInt("codigo"),
                            jsonArrayTeacher.getJSONObject(i).getString("nombre"),
                            jsonArrayTeacher.getJSONObject(i).getString("apellido"),
                            jsonArrayTeacher.getJSONObject(i).getString("asignatura")
                    ));
                    //Log.v(TAG, "obtenerData teacherList: " + teacherList);
                }
                saveTeacher(teacherList);
            }


            JSONArray jsonArrayStudent = jsonObject.getJSONArray("alumnos");
            Log.v(TAG, "obtenerData jsonArrayStudent: " + jsonArrayStudent);
            if (jsonArrayStudent != null && jsonArrayStudent.length() > 0) {
                //dataBase.studentDao().deleteAll();
                List<Student> studentList = new ArrayList<>();
                for (int i = 0; i < jsonArrayStudent.length(); i++) {
                    JSONObject jsonObject1 = jsonArrayStudent.getJSONObject(i);
                    //Log.v(TAG, "obtenerData jsonObject1: " + jsonObject1);
                    Student student = new Student(
                            jsonObject1.getInt("codigo"),
                            jsonObject1.getString("nombre"),
                            jsonObject1.getString("apellido")
                    );
                    JSONArray jsonArrayCourseForStudent = jsonObject1.getJSONArray("Asignaturas");
                    //Log.v(TAG, "obtenerData jsonArrayCourseForStudent: " + jsonArrayCourseForStudent);
                    List<String> listCourses = new ArrayList<>();
                    for (int j = 0; j < jsonArrayCourseForStudent.length(); j++) {
                        String tmp = jsonArrayCourseForStudent.getString(j);
                        listCourses.add(tmp);
                        //Log.v(TAG, "obtenerData courseForStudent: " + courseForStudent);
                    }
                    Log.v(TAG, "obtenerData listCourses: " + listCourses);
                    student.setCourses(listCourses.toString());
                    studentList.add(student);
                }
                //Log.v(TAG, "obtenerData studentList: " + studentList);
                saveStudent(studentList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v(TAG, "obtenerData e: " + e.getMessage());
        }
    }

    public String loadJSONFromAsset(String flName) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(flName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.v(TAG, "loadJSONFromAsset json ok");
        } catch (IOException ex) {
            Log.v(TAG, "loadJSONFromAsset Error: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void saveCourse(List<Course> misAsignaturas) {
        //Log.v(TAG, "saveCourse course: " + course);
        getDisposables().add(
                dataBase.courseDao().insert(misAsignaturas)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .subscribe(result -> {
                                    Log.e(TAG, "saveCourse subscribe result: " + result);
                                }, throwable -> {
                                    Log.e(TAG, "saveCourse throwable: " + throwable.getMessage());
                                }
                        )
        );
    }

    private void saveTeacher(List<Teacher> teacherList) {
        //Log.v(TAG, "saveTeacher teacherList: " + teacherList);
        getDisposables().add(
                dataBase.teacherDao().insert(teacherList)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .subscribe(result -> {
                                    Log.e(TAG, "saveCourse subscribe result: " + result);
                                }, throwable -> {
                                    Log.e(TAG, "saveCourse throwable: " + throwable.getMessage());
                                }
                        )
        );
    }

    private void saveStudent(List<Student> studentList) {
        //Log.v(TAG, "saveTeacher studentList: " + studentList);
        getDisposables().add(
                dataBase.studentDao().insert(studentList)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .subscribe(result -> {
                                    Log.e(TAG, "saveStudent subscribe result: " + result);
                                }, throwable -> {
                                    Log.e(TAG, "saveStudent throwable: " + throwable.getMessage());
                                }
                        )
        );
    }

    private void moveToMain() {
        Log.v(TAG, "moveToMain: ");
        startActivity(new Intent(CargarDatosActivity.this, MainActivity.class));
    }
}
