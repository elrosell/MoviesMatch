package com.example.moviematch.datos;

import com.example.moviematch.modelo.Pelicula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MotorRecomendaciones {

    public List<Pelicula> recomendar(List<Pelicula> catalogo,
                                     String tiempoSeleccionado,
                                     String estadoAnimo,
                                     String compania,
                                     List<String> generosFavoritos,
                                     List<String> plataformasSeleccionadas,
                                     List<String> etiquetasEvitar,
                                     List<String> idsDescartadas) {

        if (catalogo == null) {
            return Collections.emptyList();
        }

        Set<String> descartadas = new HashSet<>(idsDescartadas);
        List<ResultadoPelicula> resultados = new ArrayList<>();

        for (Pelicula pelicula : catalogo) {
            if (descartadas.contains(pelicula.getId())) continue;
            int puntaje = calcularPuntaje(pelicula, tiempoSeleccionado, estadoAnimo, compania, generosFavoritos, plataformasSeleccionadas, etiquetasEvitar);
            resultados.add(new ResultadoPelicula(pelicula, puntaje));
        }

        resultados.sort(Comparator.comparingInt(ResultadoPelicula::puntaje).reversed());

        List<Pelicula> seleccionadas = new ArrayList<>();
        for (int i = 0; i < resultados.size() && i < 10; i++) {
            seleccionadas.add(resultados.get(i).pelicula());
        }

        if (seleccionadas.isEmpty()) {
            for (Pelicula pelicula : catalogo) {
                if (!descartadas.contains(pelicula.getId())) {
                    seleccionadas.add(pelicula);
                    if (seleccionadas.size() == 10) break;
                }
            }
        }

        return seleccionadas;
    }

    public Pelicula recomendarAleatoria(List<Pelicula> catalogo, List<String> idsDescartadas) {
        List<Pelicula> disponibles = new ArrayList<>();
        Set<String> descartadas = new HashSet<>(idsDescartadas);
        for (Pelicula pelicula : catalogo) {
            if (!descartadas.contains(pelicula.getId())) {
                disponibles.add(pelicula);
            }
        }
        if (disponibles.isEmpty()) return null;
        Collections.shuffle(disponibles);
        return disponibles.get(0);
    }

    private int calcularPuntaje(Pelicula pelicula,
                                String tiempoSeleccionado,
                                String estadoAnimo,
                                String compania,
                                List<String> generosFavoritos,
                                List<String> plataformasSeleccionadas,
                                List<String> etiquetasEvitar) {
        int puntaje = 0;

        if (hayInterseccion(pelicula.getGeneros(), generosFavoritos)) {
            puntaje += 3;
        }

        if (estadoAnimo != null && estadoAnimo.equalsIgnoreCase(pelicula.getMoodSugerido())) {
            puntaje += 2;
        }

        puntaje += puntajePorCompania(pelicula.getGeneros(), compania);

        if (hayInterseccion(pelicula.getPlataformas(), plataformasSeleccionadas)) {
            puntaje += 2;
        }

        if (hayInterseccion(pelicula.getEtiquetas(), etiquetasEvitar)) {
            puntaje -= 5;
        }

        if (!cumpleDuracion(pelicula.getDuracionMin(), tiempoSeleccionado)) {
            puntaje -= 3;
        }

        return puntaje;
    }

    private boolean hayInterseccion(List<String> listaA, List<String> listaB) {
        if (listaA == null || listaB == null) return false;
        for (String valor : listaA) {
            for (String comparar : listaB) {
                if (valor.equalsIgnoreCase(comparar)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int puntajePorCompania(List<String> generos, String compania) {
        if (compania == null) return 0;
        Set<String> favorecidos = new HashSet<>();
        switch (compania.toLowerCase()) {
            case "familia":
                favorecidos.add("familia");
                favorecidos.add("animación");
                favorecidos.add("animacion");
                favorecidos.add("comedia");
                break;
            case "pareja":
                favorecidos.add("romance");
                favorecidos.add("comedia");
                favorecidos.add("drama");
                break;
            case "amigos":
                favorecidos.add("acción");
                favorecidos.add("accion");
                favorecidos.add("comedia");
                favorecidos.add("sci-fi");
                favorecidos.add("ciencia ficción");
                favorecidos.add("terror");
                break;
            default:
                return 0;
        }
        for (String genero : generos) {
            if (favorecidos.contains(genero.toLowerCase())) {
                return 2;
            }
        }
        return 0;
    }

    private boolean cumpleDuracion(int duracionMin, String tiempoSeleccionado) {
        if (tiempoSeleccionado == null) return true;
        String tiempo = tiempoSeleccionado.toLowerCase();
        if (tiempo.contains("30")) {
            return duracionMin <= 60;
        } else if (tiempo.contains("1.5")) {
            return duracionMin >= 90 && duracionMin <= 120;
        } else if (tiempo.contains("1 h") || tiempo.contains("1h")) {
            return duracionMin >= 60 && duracionMin <= 90;
        } else if (tiempo.contains("2")) {
            return duracionMin >= 120;
        }
        return true;
    }

    private record ResultadoPelicula(Pelicula pelicula, int puntaje) {
    }
}
