package com.example.moviematch.interfaz.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviematch.R;
import com.example.moviematch.datos.PreferenciasUsuario;
import com.example.moviematch.interfaz.configuracion.ConfiguracionInicialFragment;
import com.example.moviematch.interfaz.grupo.ModoGrupoFragment;
import com.example.moviematch.interfaz.guardadas.VerDespuesFragment;
import com.example.moviematch.interfaz.recomendaciones.RecomendacionesFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class InicioFragment extends Fragment {

    private PreferenciasUsuario preferenciasUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        preferenciasUsuario = new PreferenciasUsuario(requireContext());

        ChipGroup chipGroupTime = view.findViewById(R.id.chipGroupTime);
        ChipGroup chipGroupMood = view.findViewById(R.id.chipGroupMood);
        ChipGroup chipGroupCompany = view.findViewById(R.id.chipGroupCompany);
        MaterialButton btnBuscar = view.findViewById(R.id.btnBuscarPelicula);
        MaterialButton btnModoGrupo = view.findViewById(R.id.btnModoGrupo);
        ImageButton btnMenuOpciones = view.findViewById(R.id.btnMenuOpciones);

        restaurarSeleccion(chipGroupTime, preferenciasUsuario.obtenerTiempoDisponible());
        restaurarSeleccion(chipGroupMood, preferenciasUsuario.obtenerEstadoAnimo());
        restaurarSeleccion(chipGroupCompany, preferenciasUsuario.obtenerCompania());

        btnBuscar.setOnClickListener(v -> {
            String tiempo = obtenerTextoChipSeleccionado(chipGroupTime);
            String mood = obtenerTextoChipSeleccionado(chipGroupMood);
            String compania = obtenerTextoChipSeleccionado(chipGroupCompany);

            if (tiempo == null || mood == null || compania == null) {
                Toast.makeText(requireContext(), R.string.mensaje_seleccionar_todo, Toast.LENGTH_SHORT).show();
                return;
            }

            preferenciasUsuario.guardarSeleccionBusqueda(tiempo, mood, compania);
            navegarAFragmento(RecomendacionesFragment.nuevaInstancia(tiempo, mood, compania), true);
        });

        btnModoGrupo.setOnClickListener(v -> navegarAFragmento(new ModoGrupoFragment(), true));

        btnMenuOpciones.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(requireContext(), v);
            menu.inflate(R.menu.menu_principal);
            menu.setOnMenuItemClickListener(this::manejarAccionMenu);
            menu.show();
        });

        return view;
    }

    private void restaurarSeleccion(ChipGroup group, @Nullable String valor) {
        if (valor == null) return;
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                Object tag = chip.getTag();
                String candidato = tag != null ? tag.toString() : chip.getText().toString();
                if (valor.equalsIgnoreCase(candidato)) {
                    chip.setChecked(true);
                    return;
                }
            }
        }
    }

    @Nullable
    private String obtenerTextoChipSeleccionado(ChipGroup group) {
        int idSeleccionado = group.getCheckedChipId();
        if (idSeleccionado == View.NO_ID) {
            return null;
        }
        Chip chip = group.findViewById(idSeleccionado);
        if (chip == null) return null;
        Object tag = chip.getTag();
        return tag != null ? tag.toString() : chip.getText().toString();
    }

    private void navegarAFragmento(Fragment fragmento, boolean agregarAPila) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmento);
        if (agregarAPila) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private boolean manejarAccionMenu(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_watchlist) {
            navegarAFragmento(new VerDespuesFragment(), true);
            return true;
        } else if (id == R.id.action_settings) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.titulo_reconfigurar)
                    .setMessage(R.string.mensaje_reconfigurar)
                    .setPositiveButton(R.string.boton_reconfigurar, (dialog, which) -> navegarAFragmento(new ConfiguracionInicialFragment(), true))
                    .setNegativeButton(R.string.boton_cancelar, null)
                    .show();
            return true;
        } else if (id == R.id.action_group) {
            navegarAFragmento(new ModoGrupoFragment(), true);
            return true;
        }
        return false;
    }
}
