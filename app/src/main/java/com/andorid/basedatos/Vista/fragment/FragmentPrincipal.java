package com.andorid.basedatos.Vista.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andorid.basedatos.bbdd.DataBase;
import com.andorid.basedatos.databinding.FragmentPrincipalBinding;
import com.andorid.basedatos.Modelo.Course;
import com.andorid.basedatos.Modelo.Student;
import com.andorid.basedatos.Modelo.Teacher;
import com.andorid.basedatos.Interfaces.OnFragmentInteractionListener;
import com.andorid.basedatos.Vista.adapter.AdapterRecycler;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentPrincipal extends Fragment {

    public static final String TAG = FragmentPrincipal.class.getSimpleName();

    private FragmentPrincipalBinding binding;

    private OnFragmentInteractionListener mFragmentInteractionListener;

    DataBase dataBase;
    AdapterRecycler mAdapter;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new ClassCastException(
                    context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteractionListener = null;
    }

    public static FragmentPrincipal newInstance() {
        FragmentPrincipal fragment = new FragmentPrincipal();
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase = DataBase.getDatabase(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrincipalBinding.inflate(inflater, container, false);
        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llenarSpinner();
    }

    @Override
    public void onDestroy() {
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
                    if(mFragmentInteractionListener!=null){
                        mFragmentInteractionListener.onAddFragmentToStack(
                                FragmentDetailStudent.newInstance(item),
                                true,
                                true,
                                FragmentDetailStudent.TAG
                                );
                    }
                }
                return false;
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
                                    for (Course course : courseList) {
                                        tmpList.add(course.getCourse());
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                            getActivity(),
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
        getActivity().runOnUiThread(new Runnable() {
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setData(viewTeacher);
            }
        });
    }
}
