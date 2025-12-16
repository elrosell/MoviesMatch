package com.example.moviematch.interfaz.recomendaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviematch.R;
import com.example.moviematch.datos.MotorRecomendaciones;
import com.example.moviematch.datos.PreferenciasUsuario;
import com.example.moviematch.datos.RepositorioPeliculas;
import com.example.moviematch.datos.db.BaseDatosApp;
import com.example.moviematch.datos.db.entidad.PeliculaDescartadaEntity;
import com.example.moviematch.interfaz.detalle.DetallePeliculaFragment;
import com.example.moviematch.modelo.Pelicula;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RecomendacionesFragment extends Fragment {

    private static final String ARG_TIEMPO = "arg_tiempo";
    private static final String ARG_MOOD = "arg_mood";
    private static final String ARG_COMPANIA = "arg_compania";

    private String tiempoSeleccionado;
    private String estadoAnimo;
    private String compania;
    private AdaptadorPeliculas adaptadorPeliculas;
    private View vistaRaiz;

    public static RecomendacionesFragment nuevaInstancia(String tiempo, String mood, String compania) {
        RecomendacionesFragment fragment = new RecomendacionesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TIEMPO, tiempo);
        args.putString(ARG_MOOD, mood);
        args.putString(ARG_COMPANIA, compania);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            tiempoSeleccionado = args.getString(ARG_TIEMPO);
            estadoAnimo = args.getString(ARG_MOOD);
            compania = args.getString(ARG_COMPANIA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vistaRaiz = inflater.inflate(R.layout.fragment_results, container, false);
        RecyclerView recyclerView = vistaRaiz.findViewById(R.id.recyclerMovies);

        adaptadorPeliculas = new AdaptadorPeliculas(
                this::abrirDetalle,
                pelicula -> {
                    // no long press en recomendaciones
                }
        );
        recyclerView.setAdapter(adaptadorPeliculas);

        cargarRecomendaciones();
        return vistaRaiz;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarRecomendaciones();
    }

    private void cargarRecomendaciones() {
        RepositorioPeliculas repositorioPeliculas = new RepositorioPeliculas(requireContext());
        PreferenciasUsuario preferenciasUsuario = new PreferenciasUsuario(requireContext());
        MotorRecomendaciones motor = new MotorRecomendaciones();

        List<PeliculaDescartadaEntity> descartadas = BaseDatosApp.obtenerInstancia(requireContext())
                .descartadasDao()
                .obtenerTodo();
        List<String> idsDescartadas = new ArrayList<>();
        for (PeliculaDescartadaEntity entity : descartadas) {
            idsDescartadas.add(entity.getId());
        }

        List<Pelicula> recomendadas = motor.recomendar(
                repositorioPeliculas.obtenerPeliculas(),
                tiempoSeleccionado,
                estadoAnimo,
                compania,
                preferenciasUsuario.obtenerGenerosFavoritos(),
                preferenciasUsuario.obtenerPlataformas(),
                preferenciasUsuario.obtenerEtiquetasAEvitar(),
                idsDescartadas
        );

        adaptadorPeliculas.actualizarPeliculas(recomendadas);

        if (recomendadas.isEmpty()) {
            Toast.makeText(requireContext(), R.string.mensaje_sin_resultados, Toast.LENGTH_LONG).show();
        } else if (vistaRaiz != null) {
            Snackbar.make(vistaRaiz, getString(R.string.mensaje_encontradas, recomendadas.size()), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void abrirDetalle(Pelicula pelicula) {
        DetallePeliculaFragment fragment = DetallePeliculaFragment.nuevaInstancia(pelicula);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null);
        transaction.commit();
    }
}
