package com.example.moviematch.interfaz.guardadas;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviematch.R;
import com.example.moviematch.datos.db.BaseDatosApp;
import com.example.moviematch.datos.db.entidad.PeliculaGuardadaEntity;
import com.example.moviematch.interfaz.detalle.DetallePeliculaFragment;
import com.example.moviematch.interfaz.recomendaciones.AdaptadorPeliculas;
import com.example.moviematch.modelo.Pelicula;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerDespuesFragment extends Fragment {

    private AdaptadorPeliculas adaptadorPeliculas;
    private View vistaRaiz;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vistaRaiz = inflater.inflate(R.layout.fragment_watchlist, container, false);
        RecyclerView recyclerView = vistaRaiz.findViewById(R.id.recyclerWatchlist);
        adaptadorPeliculas = new AdaptadorPeliculas(this::abrirDetalle, this::eliminarPelicula);
        recyclerView.setAdapter(adaptadorPeliculas);
        cargarWatchlist();
        return vistaRaiz;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarWatchlist();
    }

    private void cargarWatchlist() {
        List<PeliculaGuardadaEntity> guardadas = BaseDatosApp.obtenerInstancia(requireContext())
                .watchlistDao()
                .obtenerTodo();
        List<Pelicula> peliculas = new ArrayList<>();
        for (PeliculaGuardadaEntity entity : guardadas) {
            Pelicula pelicula = new Pelicula();
            pelicula.setId(entity.getId());
            pelicula.setTitulo(entity.getTitulo());
            pelicula.setAnio(entity.getAnio());
            pelicula.setDuracionMin(entity.getDuracionMin());
            pelicula.setGeneros(convertirALista(entity.getGeneros()));
            pelicula.setPlataformas(convertirALista(entity.getPlataformas()));
            pelicula.setEtiquetas(Collections.emptyList());
            pelicula.setMoodSugerido(entity.getMoodSugerido());
            pelicula.setSinopsis(entity.getSinopsis());
            pelicula.setPosterUrl(entity.getPosterUrl());
            peliculas.add(pelicula);
        }
        adaptadorPeliculas.actualizarPeliculas(peliculas);
    }

    private List<String> convertirALista(String texto) {
        if (TextUtils.isEmpty(texto)) return Collections.emptyList();
        String[] partes = texto.split(",");
        List<String> lista = new ArrayList<>();
        for (String parte : partes) {
            lista.add(parte.trim());
        }
        return lista;
    }

    private void eliminarPelicula(Pelicula pelicula) {
        BaseDatosApp.obtenerInstancia(requireContext()).watchlistDao().eliminarPorId(pelicula.getId());
        Snackbar.make(vistaRaiz, R.string.mensaje_eliminada_watchlist, Snackbar.LENGTH_SHORT).show();
        cargarWatchlist();
    }

    private void abrirDetalle(Pelicula pelicula) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, DetallePeliculaFragment.nuevaInstancia(pelicula))
                .addToBackStack(null);
        transaction.commit();
    }
}
