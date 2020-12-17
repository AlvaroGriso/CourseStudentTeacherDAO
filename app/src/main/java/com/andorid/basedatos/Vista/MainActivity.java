package com.andorid.basedatos.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andorid.basedatos.Modelo.Course;
import com.andorid.basedatos.Modelo.bbdd.DataBase;
import com.andorid.basedatos.databinding.ActivityMainBinding;
import com.andorid.basedatos.Modelo.Student;
import com.andorid.basedatos.Modelo.Teacher;
import com.andorid.basedatos.Modelo.bbdd.Constants;
import com.andorid.basedatos.adapter.AdapterRecycler;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();

    ActivityMainBinding binding;
    DataBase dataBase;
    AdapterRecycler mAdapter;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dataBase = DataBase.getDatabase(this);
        setupRecyclerView();
        llenarSpinner();
        Log.v(TAG, "onCreate: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables != null) {
            disposables.dispose();
        }
    }

    private void setupRecyclerView() {
        mAdapter = new AdapterRecycler();
        mAdapter.setOnListener(new AdapterRecycler.OnItemClickListener() {
            @Override
            public boolean onItemClick(View view, Student item, int position, boolean longPress) {
                if (!longPress) {
                    Log.d(TAG, "item: " + item);
                    movetToDetailStudent(item);
                }
                return false;
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(mLayoutManager);
        binding.recycler.setAdapter(mAdapter);
    }

    private CompositeDisposable getDisposables() {
        if (disposables == null)
            disposables = new CompositeDisposable();
        return disposables;
    }

    private void llenarSpinner() {
        getDisposables().add(
                dataBase.courseDao().getAllCourse()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .subscribe(courseList -> {
                                    Log.e(TAG, "llenarSpinner subscribe courseList: " + courseList);
                                    ArrayList<String> tmpList = new ArrayList<>();
                                    tmpList.add("Seleccione");
                                    for (Course misAsignaturas : courseList) {
                                        tmpList.add(misAsignaturas.getCourse());
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                            this,
                                            android.R.layout.simple_spinner_dropdown_item, tmpList);
                                    binding.spSelectCourse.setAdapter(arrayAdapter);
                                }, throwable -> {
                                    Log.e(TAG, "llenarSpinner throwable: " + throwable.getMessage());
                                }
                        )
        );
        binding.spSelectCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String select = parent.getItemAtPosition(position).toString();
                Log.e(TAG, "llenarSpinner select: " + select);
                viewTeacher(null);
                obtainTeacher(select);
                obtainStudent(select);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void obtainTeacher(String course) {
        getDisposables().add(
                dataBase.teacherDao().getTeacherForCourse(course)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .subscribe(teacher -> {
                                    Log.e(TAG, "obtainTeacher subscribe teacher: " + teacher);
                                    viewTeacher(teacher);
                                }, throwable -> {
                                    Log.e(TAG, "obtainTeacher throwable: " + throwable.getMessage());
                                }
                        )
        );
    }

    private void viewTeacher(Teacher teacher) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (teacher != null) {
                    binding.txtNameTeacher.setText(teacher.getNombre());
                    binding.txtLastNameTeacher.setText(teacher.getApellido());
                    binding.txtCourse.setText(teacher.getAsignatura());
                } else {
                    binding.txtNameTeacher.setText("");
                    binding.txtLastNameTeacher.setText("");
                    binding.txtCourse.setText("");
                }
            }
        });
    }

    private void obtainStudent(String course) {
        getDisposables().add(
                dataBase.studentDao().getStudentForCourse()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .subscribe(student -> {
                                    Log.e(TAG, "obtainStudent subscribe student: " + student);
                                    List<Student> tmpList = new ArrayList<>();
                                    for (Student student1 : student) {
                                        if (student1.getCourses().contains(course)) {
                                            tmpList.add(student1);
                                        }
                                    }
                                    viewStudent(tmpList);
                                }, throwable -> {
                                    Log.e(TAG, "obtainStudent throwable: " + throwable.getMessage());
                                }
                        )
        );
    }

    private void viewStudent(List<Student> viewTeacher) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setData(viewTeacher);
            }
        });
    }

    private void movetToDetailStudent(Student student) {
        Intent intent = new Intent(MainActivity.this, CardEstudianteActivity.class);
        intent.putExtra(Constants.Extras.EXTRA_STUDENT, student);
        startActivity(intent);
    }
}
