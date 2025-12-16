package com.example.moviematch.datos.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.moviematch.datos.db.entidad.PeliculaDescartadaEntity;

import java.util.List;

@Dao
public interface DescartadasDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertar(PeliculaDescartadaEntity pelicula);

    @Query("SELECT * FROM peliculas_descartadas")
    List<PeliculaDescartadaEntity> obtenerTodo();
}
