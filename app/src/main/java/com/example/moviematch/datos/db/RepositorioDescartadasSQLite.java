package com.example.moviematch.datos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.HashSet;
import java.util.Set;

public class RepositorioDescartadasSQLite {

    private final Base base;

    public RepositorioDescartadasSQLite(Context context) {
        base = new Base(context.getApplicationContext());
    }

    public boolean descartarPelicula(String peliculaId) {
        SQLiteDatabase db = null;
        ContentValues valores = new ContentValues();
        valores.put("pelicula_id", peliculaId);
        valores.put("fecha_descartado", System.currentTimeMillis());
        try {
            db = base.getWritableDatabase();
            long resultado = db.insertWithOnConflict(
                    Base.TABLA_DESCARTADAS,
                    null,
                    valores,
                    SQLiteDatabase.CONFLICT_IGNORE
            );
            return resultado != -1;
        } catch (SQLiteException e) {
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public boolean estaDescartada(String peliculaId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = base.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT pelicula_id FROM " + Base.TABLA_DESCARTADAS + " WHERE pelicula_id = ?",
                    new String[]{peliculaId}
            );
            return cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public Set<String> obtenerIdsDescartadas() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Set<String> ids = new HashSet<>();
        try {
            db = base.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT pelicula_id FROM " + Base.TABLA_DESCARTADAS,
                    null
            );
            while (cursor.moveToNext()) {
                ids.add(cursor.getString(0));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return ids;
    }
}
