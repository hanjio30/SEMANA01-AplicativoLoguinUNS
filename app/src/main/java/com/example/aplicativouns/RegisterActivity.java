package com.example.aplicativouns;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private Spinner spinnerFacultad, spinnerEscuela;
    private TextInputEditText etNombres, etApellidos, etCodigo, etDni, etCorreo, etPasswordRegistro;
    private MaterialButton btnRegistrarse;
    private FloatingActionButton fabVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inicializarVistas();
        configurarSpinners();
        configurarEventos();
    }

    private void inicializarVistas() {
        spinnerFacultad = findViewById(R.id.spinnerFacultad);
        spinnerEscuela = findViewById(R.id.spinnerEscuela);
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etCodigo = findViewById(R.id.etCodigo);
        etDni = findViewById(R.id.etDni);
        etCorreo = findViewById(R.id.etCorreo);
        etPasswordRegistro = findViewById(R.id.etPasswordRegistro);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        fabVolver = findViewById(R.id.fabVolver);
    }

    private void configurarSpinners() {
        // Configurar spinner de facultades
        ArrayAdapter<CharSequence> facultadAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.facultades,
                android.R.layout.simple_spinner_item
        );
        facultadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFacultad.setAdapter(facultadAdapter);

        // Configurar spinner de escuelas
        List<String> escuelas = new ArrayList<>();
        ArrayAdapter<String> escuelaAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                escuelas
        );
        escuelaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEscuela.setAdapter(escuelaAdapter);

        // Listener para cambio de facultad
        spinnerFacultad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarEscuelas(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void actualizarEscuelas(int facultadPosition) {
        List<String> escuelas = new ArrayList<>();
        int arrayResource = 0;

        switch (facultadPosition) {
            case 1: // Educación y Humanidades
                arrayResource = R.array.escuelas_educacion;
                break;
            case 2: // Ciencia
                arrayResource = R.array.escuelas_ciencias;
                break;
            case 3: // Ingeniería
                arrayResource = R.array.escuelas_ingenieria;
                break;
        }

        if (arrayResource != 0) {
            escuelas.addAll(Arrays.asList(getResources().getStringArray(arrayResource)));
        } else {
            escuelas.add("Seleccione una facultad primero");
        }

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerEscuela.getAdapter();
        adapter.clear();
        adapter.addAll(escuelas);
        adapter.notifyDataSetChanged();
    }

    private void configurarEventos() {
        fabVolver.setOnClickListener(v -> finish());

        btnRegistrarse.setOnClickListener(v -> {
            if (validarCampos()) {
                // Guardar datos en SharedPreferences
                SharedPreferences preferences = getSharedPreferences("Usuarios", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nombres", etNombres.getText().toString().trim());
                editor.putString("apellidos", etApellidos.getText().toString().trim());
                editor.putString("codigo", etCodigo.getText().toString().trim());
                editor.putString("dni", etDni.getText().toString().trim());
                editor.putString("correo", etCorreo.getText().toString().trim());
                editor.putString("password", etPasswordRegistro.getText().toString().trim());
                // Añadir estas líneas para guardar Facultad y Escuela
                editor.putString("facultad", spinnerFacultad.getSelectedItem().toString());
                editor.putString("escuela", spinnerEscuela.getSelectedItem().toString());
                editor.apply();

                mostrarMensaje("Registro exitoso");
                finish();
            }
        });
    }

    private boolean validarCampos() {
        // Validación mejorada
        if (etNombres.getText().toString().trim().isEmpty()) {
            mostrarMensaje("Ingrese sus nombres");
            return false;
        }
        if (etApellidos.getText().toString().trim().isEmpty()) {
            mostrarMensaje("Ingrese sus apellidos");
            return false;
        }
        if (etCodigo.getText().toString().trim().isEmpty()) {
            mostrarMensaje("Ingrese su código de estudiante");
            return false;
        }
        if (etDni.getText().toString().trim().length() != 8) {
            mostrarMensaje("DNI debe tener 8 dígitos");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etCorreo.getText().toString().trim()).matches()) {
            mostrarMensaje("Ingrese un correo válido");
            return false;
        }
        if (etPasswordRegistro.getText().toString().trim().length() < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        if (spinnerFacultad.getSelectedItemPosition() == 0) {
            mostrarMensaje("Seleccione una facultad");
            return false;
        }
        if (spinnerEscuela.getSelectedItemPosition() == 0) {
            mostrarMensaje("Seleccione una escuela");
            return false;
        }
        return true;
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}