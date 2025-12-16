package com.example.moviematch.datos.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moviematch.datos.db.dao.DescartadasDao;
import com.example.moviematch.datos.db.dao.WatchlistDao;
import com.example.moviematch.datos.db.entidad.PeliculaDescartadaEntity;
import com.example.moviematch.datos.db.entidad.PeliculaGuardadaEntity;

@Database(entities = {PeliculaGuardadaEntity.class, PeliculaDescartadaEntity.class}, version = 1, exportSchema = false)
public abstract class BaseDatosApp extends RoomDatabase {

    private static final String NOMBRE_BD = "peliculas_app.db";
    private static BaseDatosApp instancia;

    public abstract WatchlistDao watchlistDao();

    public abstract DescartadasDao descartadasDao();

    public static synchronized BaseDatosApp obtenerInstancia(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(context.getApplicationContext(), BaseDatosApp.class, NOMBRE_BD)
                    .allowMainThreadQueries()
                    .build();
        }
        return instancia;
    }
}
