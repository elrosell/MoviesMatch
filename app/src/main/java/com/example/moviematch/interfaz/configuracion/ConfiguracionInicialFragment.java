package com.example.moviematch.interfaz.configuracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviematch.R;
import com.example.moviematch.datos.PreferenciasUsuario;
import com.example.moviematch.interfaz.inicio.InicioFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionInicialFragment extends Fragment {

    private PreferenciasUsuario preferenciasUsuario;
    private ChipGroup chipGeneros;
    private ChipGroup chipPlataformas;
    private ChipGroup chipEvitar;
    private View vistaRaiz;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vistaRaiz = inflater.inflate(R.layout.fragment_onboarding, container, false);
        preferenciasUsuario = new PreferenciasUsuario(requireContext());
        chipGeneros = vistaRaiz.findViewById(R.id.chipGroupGenres);
        chipPlataformas = vistaRaiz.findViewById(R.id.chipGroupPlatforms);
        chipEvitar = vistaRaiz.findViewById(R.id.chipGroupAvoid);

        MaterialButton btnSaltar = vistaRaiz.findViewById(R.id.btnSkipOnboarding);
        MaterialButton btnContinuar = vistaRaiz.findViewById(R.id.btnFinishOnboarding);

        marcarSeleccionGuardada();

        btnSaltar.setOnClickListener(v -> {
            preferenciasUsuario.guardarPreferencias(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            irAInicio();
        });

        btnContinuar.setOnClickListener(v -> {
            List<String> generos = obtenerSeleccionMultiple(chipGeneros);
            List<String> plataformas = obtenerSeleccionMultiple(chipPlataformas);
            List<String> evitar = obtenerSeleccionMultiple(chipEvitar);
            preferenciasUsuario.guardarPreferencias(generos, plataformas, evitar);
            Snackbar.make(vistaRaiz, R.string.mensaje_preferencias_guardadas, Snackbar.LENGTH_SHORT).show();
            irAInicio();
        });

        return vistaRaiz;
    }

    private void marcarSeleccionGuardada() {
        marcarChipsSeleccionados(chipGeneros, preferenciasUsuario.obtenerGenerosFavoritos());
        marcarChipsSeleccionados(chipPlataformas, preferenciasUsuario.obtenerPlataformas());
        marcarChipsSeleccionados(chipEvitar, preferenciasUsuario.obtenerEtiquetasAEvitar());
    }

    private void marcarChipsSeleccionados(ChipGroup grupo, List<String> valores) {
        for (int i = 0; i < grupo.getChildCount(); i++) {
            View child = grupo.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                chip.setChecked(valores.contains(chip.getText().toString()));
            }
        }
    }

    private List<String> obtenerSeleccionMultiple(ChipGroup group) {
        List<String> seleccion = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                if (chip.isChecked()) {
                    seleccion.add(chip.getText().toString());
                }
            }
        }
        return seleccion;
    }

    private void irAInicio() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new InicioFragment())
                .addToBackStack(null);
        transaction.commit();
    }
}
