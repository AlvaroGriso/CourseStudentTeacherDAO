package com.andorid.basedatos.Vista.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andorid.basedatos.databinding.FragmentDetailStudentBinding;
import com.andorid.basedatos.Modelo.Student;
import com.andorid.basedatos.bbdd.Constants;
import com.andorid.basedatos.Interfaces.OnFragmentInteractionListener;

public class FragmentDetailStudent extends Fragment {

    public static final String TAG = FragmentDetailStudent.class.getSimpleName();

    FragmentDetailStudentBinding binding;

    private OnFragmentInteractionListener mFragmentInteractionListener;

    private Student mStudent;

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

    public static FragmentDetailStudent newInstance(Student student) {
        FragmentDetailStudent fragment = new FragmentDetailStudent();
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putSerializable(Constants.Extras.EXTRA_STUDENT, student);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudent = (Student) getArguments().getSerializable(Constants.Extras.EXTRA_STUDENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailStudentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewStudent();
    }

    private void viewStudent() {
        if(mStudent!=null){
            binding.txtCode.setText(String.valueOf(mStudent.getCodigo()));
            binding.txtNameStudent.setText(mStudent.getNombre());
            binding.txtLastNameStudent.setText(mStudent.getApellido());
        }
    }
}
