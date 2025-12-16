package com.example.moviematch.modelo;

import java.io.Serializable;
import java.util.List;

public class Pelicula implements Serializable {
    private String id;
    private String titulo;
    private int anio;
    private int duracionMin;
    private List<String> generos;
    private List<String> plataformas;
    private List<String> etiquetas;
    private String moodSugerido;
    private String sinopsis;
    private String posterUrl;

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getAnio() {
        return anio;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public List<String> getPlataformas() {
        return plataformas;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public String getMoodSugerido() {
        return moodSugerido;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    public void setPlataformas(List<String> plataformas) {
        this.plataformas = plataformas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public void setMoodSugerido(String moodSugerido) {
        this.moodSugerido = moodSugerido;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
