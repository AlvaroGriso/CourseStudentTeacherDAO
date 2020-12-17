package com.andorid.basedatos.Vista.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.andorid.basedatos.R;
import com.andorid.basedatos.databinding.ActivityMainBinding;
import com.andorid.basedatos.Interfaces.OnFragmentInteractionListener;
import com.andorid.basedatos.Vista.fragment.FragmentPrincipal;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    String TAG = MainActivity.class.getSimpleName();
   //Recordatorio: Tengo que añadir OnBackPressed() y Toast para notificar de acciones.
    ActivityMainBinding binding;
    private FragmentManager mFragManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mFragManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            onAddFragmentToStack(FragmentPrincipal.newInstance(),
                    false,
                    false,
                    FragmentPrincipal.TAG);
        }

        Log.v(TAG, "onCreate: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAddFragmentToStack(@NonNull Fragment fragment, boolean replaceFragment,
                                     boolean addToBackStack, @NonNull String tag) {
        FragmentTransaction ft = mFragManager.beginTransaction();

        if (replaceFragment)
            ft.replace(R.id.fragment_container, fragment, tag);
        else
            ft.add(R.id.fragment_container, fragment, tag);

        if (addToBackStack) ft.addToBackStack(null);

        try {
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onRemoveFragmentToStack(boolean withBackPressed) {

    }
}
