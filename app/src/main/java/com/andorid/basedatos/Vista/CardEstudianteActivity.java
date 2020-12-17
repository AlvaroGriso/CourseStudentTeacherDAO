package com.andorid.basedatos.Vista;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andorid.basedatos.databinding.ActivityCardEstudianteBinding;
import com.andorid.basedatos.Modelo.Student;
import com.andorid.basedatos.Modelo.bbdd.Constants;


public class CardEstudianteActivity extends AppCompatActivity {

    ActivityCardEstudianteBinding binding;
    Student mStudent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardEstudianteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mStudent = (Student) getIntent().getExtras().getSerializable(Constants.Extras.EXTRA_STUDENT);
        viewStudent(mStudent);
    }

    private void viewStudent(Student student) {
        binding.txtCode.setText(String.valueOf(student.getCodigo()));
        binding.txtNameStudent.setText(student.getNombre());
        binding.txtLastNameStudent.setText(student.getApellido());
    }
}
