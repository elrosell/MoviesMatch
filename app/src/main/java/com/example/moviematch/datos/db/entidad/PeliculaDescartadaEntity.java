package com.example.moviematch.datos.db.entidad;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "peliculas_descartadas")
public class PeliculaDescartadaEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private long fechaDescartada;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public long getFechaDescartada() {
        return fechaDescartada;
    }

    public void setFechaDescartada(long fechaDescartada) {
        this.fechaDescartada = fechaDescartada;
    }
}
