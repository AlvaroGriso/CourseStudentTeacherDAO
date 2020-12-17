package com.andorid.basedatos.Interfaces;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface OnFragmentInteractionListener {

    void onAddFragmentToStack(@NonNull Fragment fragment, boolean replaceFragment,
                              boolean addToBackStack, @NonNull String tag);

    void onRemoveFragmentToStack(boolean withBackPressed);
}
