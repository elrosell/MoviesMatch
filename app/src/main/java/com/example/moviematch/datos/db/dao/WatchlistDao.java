package com.example.moviematch.datos.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.moviematch.datos.db.entidad.PeliculaGuardadaEntity;

import java.util.List;

@Dao
public interface WatchlistDao {

    @Query("SELECT * FROM peliculas_guardadas")
    List<PeliculaGuardadaEntity> obtenerTodo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(PeliculaGuardadaEntity pelicula);

    @Delete
    void eliminar(PeliculaGuardadaEntity pelicula);

    @Query("DELETE FROM peliculas_guardadas WHERE id = :id")
    void eliminarPorId(String id);
}
