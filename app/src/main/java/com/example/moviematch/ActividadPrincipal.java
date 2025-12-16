package com.example.moviematch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.moviematch.datos.modelo.Pelicula;
import com.example.moviematch.datos.preferencias.GestorPreferenciasUsuario;
import com.example.moviematch.ui.detalle.DetallePeliculaFragment;
import com.example.moviematch.ui.inicio.InicioFragment;
import com.example.moviematch.ui.modogrupo.ModoGrupoFragment;
import com.example.moviematch.ui.onboarding.OnboardingFragment;
import com.example.moviematch.ui.recomendaciones.RecomendacionesFragment;
import com.example.moviematch.ui.verdespues.VerDespuesFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class ActividadPrincipal extends AppCompatActivity {

    private GestorPreferenciasUsuario gestorPreferenciasUsuario;
    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        gestorPreferenciasUsuario = new GestorPreferenciasUsuario(this);

        topAppBar = findViewById(R.id.topAppBar);
        if (topAppBar != null) {
            setSupportActionBar(topAppBar);
            topAppBar.setVisibility(View.VISIBLE);
        }

        View root = findViewById(R.id.rootMain);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        if (savedInstanceState == null) {
            if (gestorPreferenciasUsuario.onboardingCompletado()) {
                mostrarFragmento(new InicioFragment(), false);
            } else {
                mostrarFragmento(new OnboardingFragment(), false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_watchlist) {
            irAVerDespues();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public GestorPreferenciasUsuario obtenerGestorPreferenciasUsuario() {
        return gestorPreferenciasUsuario;
    }

    public void irAOnboarding() {
        mostrarFragmento(new OnboardingFragment(), false);
    }

    public void irAInicio() {
        mostrarFragmento(new InicioFragment(), false);
    }

    public void irARecomendaciones(String mood, Integer duracionMaxMinutos, String compania) {
        RecomendacionesFragment fragment = RecomendacionesFragment.nuevaInstancia(mood, duracionMaxMinutos, compania);
        mostrarFragmento(fragment, true);
    }

    public void irADetalle(Pelicula pelicula) {
        DetallePeliculaFragment fragment = DetallePeliculaFragment.nuevaInstancia(pelicula);
        mostrarFragmento(fragment, true);
    }

    public void irAVerDespues() {
        mostrarFragmento(new VerDespuesFragment(), true);
    }

    public void irAModoGrupo() {
        mostrarFragmento(new ModoGrupoFragment(), true);
    }

    private void mostrarFragmento(Fragment fragment, boolean agregarAStack) {
        if (agregarAStack) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
