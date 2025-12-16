package com.example.moviematch.interfaz.detalle;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.moviematch.R;
import com.example.moviematch.datos.db.BaseDatosApp;
import com.example.moviematch.datos.db.entidad.PeliculaDescartadaEntity;
import com.example.moviematch.datos.db.entidad.PeliculaGuardadaEntity;
import com.example.moviematch.modelo.Pelicula;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class DetallePeliculaFragment extends Fragment {

    private static final String ARG_PELICULA = "arg_pelicula";
    private Pelicula pelicula;

    public static DetallePeliculaFragment nuevaInstancia(Pelicula pelicula) {
        DetallePeliculaFragment fragment = new DetallePeliculaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PELICULA, pelicula);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pelicula = (Pelicula) getArguments().getSerializable(ARG_PELICULA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        if (pelicula == null) return vista;

        ImageView imgPoster = vista.findViewById(R.id.imgPosterBig);
        TextView txtTitulo = vista.findViewById(R.id.txtMovieTitleDetail);
        TextView txtMeta = vista.findViewById(R.id.txtMovieMetaDetail);
        TextView txtPlataformas = vista.findViewById(R.id.txtMoviePlatformsDetail);
        TextView txtSinopsis = vista.findViewById(R.id.txtMovieSynopsis);
        ChipGroup chipGroupTags = vista.findViewById(R.id.chipGroupTags);
        MaterialButton btnVerDespues = vista.findViewById(R.id.btnAddToWatchlist);
        MaterialButton btnNoInteresa = vista.findViewById(R.id.btnDislike);

        txtTitulo.setText(pelicula.getTitulo());
        String generos = TextUtils.join(" / ", pelicula.getGeneros() != null ? pelicula.getGeneros() : java.util.Collections.emptyList());
        String meta = getString(R.string.formato_meta_detalle, pelicula.getAnio(), pelicula.getDuracionMin(), generos);
        txtMeta.setText(meta);
        txtPlataformas.setText(getString(R.string.formato_plataformas, TextUtils.join(", ", pelicula.getPlataformas() != null ? pelicula.getPlataformas() : java.util.Collections.emptyList())));
        txtSinopsis.setText(pelicula.getSinopsis());

        Glide.with(this)
                .load(TextUtils.isEmpty(pelicula.getPosterUrl()) ? null : pelicula.getPosterUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imgPoster);

        chipGroupTags.removeAllViews();
        if (pelicula.getEtiquetas() != null) {
            for (String etiqueta : pelicula.getEtiquetas()) {
                Chip chip = (Chip) inflater.inflate(R.layout.simple_chip, chipGroupTags, false);
                chip.setText(etiqueta);
                chipGroupTags.addView(chip);
            }
        }
        Chip moodChip = (Chip) inflater.inflate(R.layout.simple_chip, chipGroupTags, false);
        moodChip.setText(String.format(Locale.getDefault(), "Mood: %s", pelicula.getMoodSugerido()));
        chipGroupTags.addView(moodChip);

        btnVerDespues.setOnClickListener(v -> guardarEnVerDespues(v));
        btnNoInteresa.setOnClickListener(v -> descartarPelicula(v));

        return vista;
    }

    private void guardarEnVerDespues(View vista) {
        PeliculaGuardadaEntity entity = new PeliculaGuardadaEntity();
        entity.setId(pelicula.getId());
        entity.setTitulo(pelicula.getTitulo());
        entity.setAnio(pelicula.getAnio());
        entity.setDuracionMin(pelicula.getDuracionMin());
        entity.setGeneros(TextUtils.join(", ", pelicula.getGeneros()));
        entity.setPlataformas(TextUtils.join(", ", pelicula.getPlataformas()));
        entity.setPosterUrl(pelicula.getPosterUrl());
        entity.setSinopsis(pelicula.getSinopsis());
        entity.setMoodSugerido(pelicula.getMoodSugerido());

        BaseDatosApp.obtenerInstancia(requireContext()).watchlistDao().insertar(entity);
        Snackbar.make(vista, R.string.mensaje_guardado_watchlist, Snackbar.LENGTH_SHORT).show();
    }

    private void descartarPelicula(View vista) {
        PeliculaDescartadaEntity entity = new PeliculaDescartadaEntity();
        entity.setId(pelicula.getId());
        entity.setFechaDescartada(System.currentTimeMillis());
        BaseDatosApp.obtenerInstancia(requireContext()).descartadasDao().insertar(entity);
        Snackbar.make(vista, R.string.mensaje_descartada, Snackbar.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
