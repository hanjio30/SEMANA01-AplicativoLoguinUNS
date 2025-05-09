package com.example.aplicativouns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    private TextView tvAppTitle, tvBienvenida, tvInformacion, tvDetallesUsuario;
    private TextView tvNombreCompleto, tvCodigo, tvDni, tvCorreo;
    private MaterialButton btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inicializarVistas();
        mostrarInformacionUsuario();
        configurarBotones();
    }

    private void inicializarVistas() {
        tvAppTitle = findViewById(R.id.tvAppTitle);
        tvBienvenida = findViewById(R.id.tvBienvenida);
        tvInformacion = findViewById(R.id.tvInformacion);
        tvDetallesUsuario = findViewById(R.id.tvDetallesUsuario);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
    }

    private void mostrarInformacionUsuario() {
        SharedPreferences preferences = getSharedPreferences("Usuarios", MODE_PRIVATE);
        String nombres = preferences.getString("nombres", "");
        String apellidos = preferences.getString("apellidos", "");
        String codigo = preferences.getString("codigo", "");
        String dni = preferences.getString("dni", "");
        String correo = preferences.getString("correo", "");
        String facultad = preferences.getString("facultad", "");
        String escuela = preferences.getString("escuela", "");

        tvAppTitle.setText(getString(R.string.bienvenido_a_uns));
        tvBienvenida.setText(getString(R.string.bienvenido) + " " + nombres);

        // Decoración mejorada con colores y formato
        if (tvDetallesUsuario != null) {
            String detalles =
                    "<b><font color='#8B0000'>Nombres:</font></b> " + nombres + "<br>" +
                            "<b><font color='#8B0000'>Apellidos:</font></b> " + apellidos + "<br>" +
                            "<b><font color='#8B0000'>Código:</font></b> " + codigo + "<br>" +
                            "<b><font color='#8B0000'>DNI:</font></b> " + dni + "<br>" +
                            "<b><font color='#8B0000'>Correo:</font></b> " + correo + "<br>" +
                            "<b><font color='#8B0000'>Facultad:</font></b> " + facultad + "<br>" +
                            "<b><font color='#8B0000'>Escuela:</font></b> " + escuela;

            tvDetallesUsuario.setText(Html.fromHtml(detalles));

            // Estilo adicional
            tvDetallesUsuario.setTextSize(16);
            tvDetallesUsuario.setPadding(24, 24, 24, 24);
            tvDetallesUsuario.setBackgroundResource(R.drawable.background_card_info); // Nuevo drawable
        }
    }

    // Método auxiliar para texto con color
    private String getColoredText(String label, String value) {
        return "<b><font color='" + getColorHex(R.color.colorPrimary) + "'>" + label + "</font></b>" +
                "<font color='" + getColorHex(R.color.colorTextValue) + "'>" + value + "</font>";
    }

    // Método para obtener color en formato HEX
    private String getColorHex(int colorRes) {
        return String.format("#%06X", (0xFFFFFF & getResources().getColor(colorRes)));
    }

    private void configurarBotones() {
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }

    private void cerrarSesion() {
        // Limpiar la sesión actual
        SharedPreferences preferences = getSharedPreferences("Usuarios", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("sesion_activa", false);
        editor.apply();

        // Redirigir a la pantalla de login
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        // Limpiar pila de actividades para que no pueda regresar a esta pantalla con el botón "Atrás"
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}