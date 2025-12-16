package com.example.moviematch.datos;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreferenciasUsuario {

    private static final String ARCHIVO_PREFS = "preferencias_usuario";
    private static final String CLAVE_DATOS = "datos_preferencias";
    private final SharedPreferences preferencias;
    private final Gson gson;
    private DatosPreferencias datos;

    public PreferenciasUsuario(Context context) {
        preferencias = context.getSharedPreferences(ARCHIVO_PREFS, Context.MODE_PRIVATE);
        gson = new Gson();
        cargarDatos();
    }

    public void guardarPreferencias(List<String> generos, List<String> plataformas, List<String> evitar) {
        datos.generosFavoritos = new ArrayList<>(generos);
        datos.plataformas = new ArrayList<>(plataformas);
        datos.etiquetasAEvitar = new ArrayList<>(evitar);
        datos.onboardingCompletado = true;
        guardarCambios();
    }

    public boolean onboardingCompletado() {
        return datos.onboardingCompletado;
    }

    public List<String> obtenerGenerosFavoritos() {
        return datos.generosFavoritos != null ? datos.generosFavoritos : Collections.emptyList();
    }

    public List<String> obtenerPlataformas() {
        return datos.plataformas != null ? datos.plataformas : Collections.emptyList();
    }

    public List<String> obtenerEtiquetasAEvitar() {
        return datos.etiquetasAEvitar != null ? datos.etiquetasAEvitar : Collections.emptyList();
    }

    public void guardarSeleccionBusqueda(String tiempo, String mood, String compania) {
        datos.tiempoDisponible = tiempo;
        datos.estadoAnimo = mood;
        datos.compania = compania;
        guardarCambios();
    }

    public String obtenerTiempoDisponible() {
        return datos.tiempoDisponible;
    }

    public String obtenerEstadoAnimo() {
        return datos.estadoAnimo;
    }

    public String obtenerCompania() {
        return datos.compania;
    }

    private void cargarDatos() {
        String json = preferencias.getString(CLAVE_DATOS, null);
        if (json == null) {
            datos = new DatosPreferencias();
            datos.generosFavoritos = new ArrayList<>();
            datos.plataformas = new ArrayList<>();
            datos.etiquetasAEvitar = new ArrayList<>();
            datos.onboardingCompletado = false;
            datos.tiempoDisponible = null;
            datos.estadoAnimo = null;
            datos.compania = null;
            guardarCambios();
        } else {
            Type tipo = new TypeToken<DatosPreferencias>() {}.getType();
            datos = gson.fromJson(json, tipo);
            if (datos.generosFavoritos == null) datos.generosFavoritos = new ArrayList<>();
            if (datos.plataformas == null) datos.plataformas = new ArrayList<>();
            if (datos.etiquetasAEvitar == null) datos.etiquetasAEvitar = new ArrayList<>();
        }
    }

    private void guardarCambios() {
        String json = gson.toJson(datos);
        preferencias.edit().putString(CLAVE_DATOS, json).apply();
    }

    private static class DatosPreferencias {
        List<String> generosFavoritos;
        List<String> plataformas;
        List<String> etiquetasAEvitar;
        boolean onboardingCompletado;
        String tiempoDisponible;
        String estadoAnimo;
        String compania;
    }
}
