package com.example.moviematch.datos;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.moviematch.modelo.Pelicula;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RepositorioPeliculas {

    private static final String ARCHIVO = "peliculas.json";
    private static final String TAG = "RepositorioPeliculas";
    private final Context contexto;
    private List<Pelicula> cachePeliculas;

    public RepositorioPeliculas(Context context) {
        this.contexto = context.getApplicationContext();
    }

    public List<Pelicula> obtenerPeliculas() {
        if (cachePeliculas == null) {
            cachePeliculas = cargarPeliculas();
        }
        return cachePeliculas;
    }

    public Pelicula buscarPorId(String id) {
        List<Pelicula> peliculas = obtenerPeliculas();
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getId().equals(id)) {
                return pelicula;
            }
        }
        return null;
    }

    private List<Pelicula> cargarPeliculas() {
        AssetManager assetManager = contexto.getAssets();
        try (InputStream inputStream = assetManager.open(ARCHIVO);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            Type tipoLista = new TypeToken<List<Pelicula>>() {
            }.getType();
            return new Gson().fromJson(reader, tipoLista);
        } catch (IOException e) {
            Log.e(TAG, "Error al cargar el archivo de películas", e);
            return Collections.emptyList();
        }
    }
}
