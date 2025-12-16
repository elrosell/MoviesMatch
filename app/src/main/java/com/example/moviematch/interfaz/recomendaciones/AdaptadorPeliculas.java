package com.example.moviematch.interfaz.recomendaciones;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviematch.R;
import com.example.moviematch.modelo.Pelicula;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorPeliculas extends RecyclerView.Adapter<AdaptadorPeliculas.PeliculaViewHolder> {

    public interface AlClicPelicula {
        void alClic(Pelicula pelicula);
    }

    public interface AlMantenerPulsado {
        void alMantener(Pelicula pelicula);
    }

    private final List<Pelicula> peliculas;
    private final AlClicPelicula listenerClic;
    private final AlMantenerPulsado listenerLargo;

    public AdaptadorPeliculas(AlClicPelicula listenerClic, AlMantenerPulsado listenerLargo) {
        this.peliculas = new ArrayList<>();
        this.listenerClic = listenerClic;
        this.listenerLargo = listenerLargo;
    }

    public void actualizarPeliculas(List<Pelicula> nuevas) {
        peliculas.clear();
        if (nuevas != null) {
            peliculas.addAll(nuevas);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new PeliculaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        Pelicula pelicula = peliculas.get(position);
        holder.txtTitulo.setText(pelicula.getTitulo());
        List<String> generos = pelicula.getGeneros() != null ? pelicula.getGeneros() : new ArrayList<>();
        String genero = generos.isEmpty() ? "" : generos.get(0);
        String info = holder.itemView.getContext().getString(R.string.formato_meta_resumen,
                pelicula.getAnio(),
                pelicula.getDuracionMin(),
                genero);
        holder.txtInfo.setText(info);
        holder.txtMood.setText(holder.itemView.getContext().getString(R.string.formato_mood, pelicula.getMoodSugerido()));
        List<String> plataformas = pelicula.getPlataformas() != null ? pelicula.getPlataformas() : new ArrayList<>();
        holder.txtPlataformas.setText(holder.itemView.getContext().getString(R.string.formato_plataformas, TextUtils.join(", ", plataformas)));

        String posterUrl = pelicula.getPosterUrl();
        Glide.with(holder.imgPoster.getContext())
                .load(TextUtils.isEmpty(posterUrl) ? null : posterUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgPoster);

        holder.itemView.setOnClickListener(v -> {
            if (listenerClic != null) listenerClic.alClic(pelicula);
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (listenerLargo != null) {
                listenerLargo.alMantener(pelicula);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

    static class PeliculaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtTitulo;
        TextView txtInfo;
        TextView txtMood;
        TextView txtPlataformas;

        PeliculaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtTitulo = itemView.findViewById(R.id.txtMovieTitle);
            txtInfo = itemView.findViewById(R.id.txtMovieInfo);
            txtMood = itemView.findViewById(R.id.txtMovieMood);
            txtPlataformas = itemView.findViewById(R.id.txtMoviePlatforms);
        }
    }
}
