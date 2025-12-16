package com.example.moviematch.interfaz.grupo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviematch.R;
import com.example.moviematch.datos.MotorRecomendaciones;
import com.example.moviematch.datos.PreferenciasUsuario;
import com.example.moviematch.datos.RepositorioPeliculas;
import com.example.moviematch.datos.db.BaseDatosApp;
import com.example.moviematch.datos.db.entidad.PeliculaDescartadaEntity;
import com.example.moviematch.modelo.Pelicula;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ModoGrupoFragment extends Fragment {

    private TextView txtResultado;
    private EditText editCodigo;
    private PreferenciasUsuario preferenciasUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_group, container, false);
        preferenciasUsuario = new PreferenciasUsuario(requireContext());

        txtResultado = vista.findViewById(R.id.txtGroupResultInfo);
        editCodigo = vista.findViewById(R.id.editRoomCode);
        MaterialButton btnCrear = vista.findViewById(R.id.btnCreateRoom);
        MaterialButton btnUnirse = vista.findViewById(R.id.btnJoinRoom);

        btnCrear.setOnClickListener(v -> crearSala());
        btnUnirse.setOnClickListener(v -> validarCodigo());

        return vista;
    }

    private void crearSala() {
        String codigo = generarCodigo();
        editCodigo.setText(codigo);
        mostrarRecomendacion(String.format(Locale.getDefault(), getString(R.string.texto_codigo_generado), codigo));
    }

    private void validarCodigo() {
        String codigo = editCodigo.getText().toString().trim();
        if (codigo.length() != 6 || !codigo.matches("[A-Z0-9]+")) {
            Toast.makeText(requireContext(), R.string.mensaje_codigo_invalido, Toast.LENGTH_SHORT).show();
            return;
        }
        mostrarRecomendacion(getString(R.string.texto_codigo_validado, codigo));
    }

    private void mostrarRecomendacion(String prefijo) {
        RepositorioPeliculas repositorioPeliculas = new RepositorioPeliculas(requireContext());
        MotorRecomendaciones motor = new MotorRecomendaciones();
        List<PeliculaDescartadaEntity> descartadas = BaseDatosApp.obtenerInstancia(requireContext())
                .descartadasDao()
                .obtenerTodo();
        List<String> idsDescartadas = new ArrayList<>();
        for (PeliculaDescartadaEntity entity : descartadas) {
            idsDescartadas.add(entity.getId());
        }

        String tiempo = preferenciasUsuario.obtenerTiempoDisponible() != null ? preferenciasUsuario.obtenerTiempoDisponible() : "1.5 h";
        String mood = preferenciasUsuario.obtenerEstadoAnimo() != null ? preferenciasUsuario.obtenerEstadoAnimo() : "Relax";
        String compania = preferenciasUsuario.obtenerCompania() != null ? preferenciasUsuario.obtenerCompania() : "Amigos";

        List<Pelicula> recomendadas = motor.recomendar(
                repositorioPeliculas.obtenerPeliculas(),
                tiempo,
                mood,
                compania,
                preferenciasUsuario.obtenerGenerosFavoritos(),
                preferenciasUsuario.obtenerPlataformas(),
                preferenciasUsuario.obtenerEtiquetasAEvitar(),
                idsDescartadas
        );

        Pelicula sugerida = recomendadas.isEmpty()
                ? motor.recomendarAleatoria(repositorioPeliculas.obtenerPeliculas(), idsDescartadas)
                : recomendadas.get(0);

        if (sugerida == null) {
            txtResultado.setText(R.string.mensaje_sin_resultados);
        } else {
            String detalle = getString(R.string.formato_recomendacion_grupo, prefijo, sugerida.getTitulo(), TextUtils.join(", ", sugerida.getPlataformas()));
            txtResultado.setText(detalle);
        }
    }

    private String generarCodigo() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            builder.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return builder.toString();
    }
}
