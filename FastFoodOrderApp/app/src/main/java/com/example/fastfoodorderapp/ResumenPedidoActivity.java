package com.example.fastfoodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ResumenPedidoActivity extends AppCompatActivity {

    private int hamburguesa, papas, refresco, helado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_pedido);

        TextView tvResumen = findViewById(R.id.tvResumen);
        TextView tvTotal = findViewById(R.id.tvTotal);
        MaterialButton btnConfirmar = findViewById(R.id.btnConfirmar);
        MaterialButton btnEditar = findViewById(R.id.btnEditar);
        MaterialButton btnCancelar = findViewById(R.id.btnCancelar);

        // Recibir datos del Intent
        Intent intentRecibido = getIntent();
        hamburguesa = intentRecibido.getIntExtra("hamburguesa", 0);
        papas = intentRecibido.getIntExtra("papas", 0);
        refresco = intentRecibido.getIntExtra("refresco", 0);
        helado = intentRecibido.getIntExtra("helado", 0);
        String resumen = intentRecibido.getStringExtra("pedido_resumen");

        tvResumen.setText(resumen);

        // Calcular y mostrar total
        double total = calcularTotal();
        tvTotal.setText(String.format("$%.2f", total));

        // CONFIRMAR PEDIDO
        btnConfirmar.setOnClickListener(v -> {
            Log.d("PEDIDO", "Pedido confirmado");

            Intent intentRespuesta = new Intent();
            intentRespuesta.putExtra("pedido_confirmado", true);
            setResult(RESULT_OK, intentRespuesta);

            Toast.makeText(this, "¡Pedido confirmado! ✅", Toast.LENGTH_LONG).show();
            finish();
        });

        // EDITAR PEDIDO (NUEVO)
        btnEditar.setOnClickListener(v -> {
            Log.d("PEDIDO", "Editar pedido - Devolviendo cantidades para modificar");

            Intent intentRespuesta = new Intent();
            intentRespuesta.putExtra("hamburguesa", hamburguesa);
            intentRespuesta.putExtra("papas", papas);
            intentRespuesta.putExtra("refresco", refresco);
            intentRespuesta.putExtra("helado", helado);
            intentRespuesta.putExtra("pedido_confirmado", false);
            setResult(RESULT_CANCELED, intentRespuesta);

            Toast.makeText(this, "Editando pedido... ✏️", Toast.LENGTH_SHORT).show();
            finish();
        });

        // CANCELAR PEDIDO
        btnCancelar.setOnClickListener(v -> {
            Log.d("PEDIDO", "Pedido cancelado completamente");

            Intent intentRespuesta = new Intent();
            intentRespuesta.putExtra("hamburguesa", 0);
            intentRespuesta.putExtra("papas", 0);
            intentRespuesta.putExtra("refresco", 0);
            intentRespuesta.putExtra("helado", 0);
            intentRespuesta.putExtra("pedido_confirmado", false);
            setResult(RESULT_CANCELED, intentRespuesta);

            Toast.makeText(this, "Pedido cancelado ❌", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Método para calcular el total
    private double calcularTotal() {
        double total = 0;
        total += hamburguesa * 5.99;
        total += papas * 3.99;
        total += refresco * 2.99;
        total += helado * 2.49;
        return total;
    }
}