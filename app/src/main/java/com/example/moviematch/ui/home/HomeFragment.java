package com.example.moviematch.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviematch.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ChipGroup chipGroupTime = view.findViewById(R.id.chipGroupTime);
        ChipGroup chipGroupMood = view.findViewById(R.id.chipGroupMood);
        ChipGroup chipGroupCompany = view.findViewById(R.id.chipGroupCompany);
        MaterialButton btnBuscar = view.findViewById(R.id.btnBuscarPelicula);
        MaterialButton btnModoGrupo = view.findViewById(R.id.btnModoGrupo);

        // TODO: manejar clics y selección de chips

        return view;
    }
}
