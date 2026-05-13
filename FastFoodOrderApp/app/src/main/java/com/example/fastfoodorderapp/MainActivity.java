package com.example.fastfoodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RESUMEN = 1;

    int hamburguesa = 0, papas = 0, refresco = 0, helado = 0;
    int lastHamburguesa = 0, lastPapas = 0, lastRefresco = 0, lastHelado = 0;

    TextView tvHamburguesa, tvPapas, tvRefresco, tvHelado;
    DrawerLayout drawerLayout;
    NavigationView navView;

    MaterialCardView cardHamburguesa, cardPapas, cardRefresco, cardHelado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // ==================== MENÚ LATERAL ====================
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                restaurarMenuCompleto();
                Toast.makeText(this, "Mostrando todo el menú 🍔", Toast.LENGTH_SHORT).show();
                Log.d("DRAWER", "Inicio - Menú restaurado");

            } else if (id == R.id.nav_mis_pedidos) {
                mostrarHistorialPedidos();
                Log.d("DRAWER", "Mis pedidos - Mostrando historial");

            } else if (id == R.id.nav_ayuda) {
                mostrarAyuda();
                Log.d("DRAWER", "Ayuda - Mostrando información");
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // Referencias contadores
        tvHamburguesa = findViewById(R.id.tvHamburguesa);
        tvPapas       = findViewById(R.id.tvPapas);
        tvRefresco    = findViewById(R.id.tvRefresco);
        tvHelado      = findViewById(R.id.tvHelado);

        // Referencias tarjetas
        cardHamburguesa = findViewById(R.id.cardHamburguesa);
        cardPapas       = findViewById(R.id.cardPapas);
        cardRefresco    = findViewById(R.id.cardRefresco);
        cardHelado      = findViewById(R.id.cardHelado);

        // Botones Hamburguesa
        findViewById(R.id.btnMasHamburguesa).setOnClickListener(v -> {
            hamburguesa++;
            actualizarTextViews();
        });
        findViewById(R.id.btnMenosHamburguesa).setOnClickListener(v -> {
            if (hamburguesa > 0) hamburguesa--;
            actualizarTextViews();
        });

        // Botones Papas
        findViewById(R.id.btnMasPapas).setOnClickListener(v -> {
            papas++;
            actualizarTextViews();
        });
        findViewById(R.id.btnMenosPapas).setOnClickListener(v -> {
            if (papas > 0) papas--;
            actualizarTextViews();
        });

        // Botones Refresco
        findViewById(R.id.btnMasRefresco).setOnClickListener(v -> {
            refresco++;
            actualizarTextViews();
        });
        findViewById(R.id.btnMenosRefresco).setOnClickListener(v -> {
            if (refresco > 0) refresco--;
            actualizarTextViews();
        });

        // Botones Helado
        findViewById(R.id.btnMasHelado).setOnClickListener(v -> {
            helado++;
            actualizarTextViews();
        });
        findViewById(R.id.btnMenosHelado).setOnClickListener(v -> {
            if (helado > 0) helado--;
            actualizarTextViews();
        });

        // FAB Carrito
        findViewById(R.id.fabCarrito).setOnClickListener(v -> {
            Log.d("CARRITO", "Click en carrito");
            if (hamburguesa == 0 && papas == 0 && refresco == 0 && helado == 0) {
                Toast.makeText(this, "Agrega algo al pedido 🛒", Toast.LENGTH_SHORT).show();
                return;
            }
            guardarUltimoPedido();
            irAResumen();
        });
    }

    // ==================== BÚSQUEDA ====================

    private void activarBusqueda() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("🔍 Buscar producto");

        final EditText input = new EditText(this);

        input.setHint("Escribe un producto...");
        input.setPadding(40, 30, 40, 30);

        builder.setView(input);

        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                filtrarProductos(
                        s.toString().toLowerCase().trim()
                );
            }
        });

        builder.setPositiveButton("Aceptar", null);

        builder.setNegativeButton("Cerrar", (dialog, which) -> {

            restaurarMenuCompleto();
        });

        builder.setOnCancelListener(dialog -> {

            restaurarMenuCompleto();
        });

        builder.show();
    }

    private void restaurarMenuCompleto() {
        filtrarProductos("");

        Toast.makeText(
                this,
                "Menú restaurado 🍔",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void filtrarProductos(String query) {
        cardHamburguesa.setVisibility(query.isEmpty() || "hamburguesa".contains(query) ? View.VISIBLE : View.GONE);
        cardPapas.setVisibility      (query.isEmpty() || "papas".contains(query)        ? View.VISIBLE : View.GONE);
        cardRefresco.setVisibility   (query.isEmpty() || "refresco".contains(query)     ? View.VISIBLE : View.GONE);
        cardHelado.setVisibility     (query.isEmpty() || "helado".contains(query)       ? View.VISIBLE : View.GONE);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void actualizarTextViews() {
        tvHamburguesa.setText(String.valueOf(hamburguesa));
        tvPapas.setText(String.valueOf(papas));
        tvRefresco.setText(String.valueOf(refresco));
        tvHelado.setText(String.valueOf(helado));
    }

    private void guardarUltimoPedido() {
        lastHamburguesa = hamburguesa;
        lastPapas = papas;
        lastRefresco = refresco;
        lastHelado = helado;
    }

    private void restaurarUltimoPedido() {
        hamburguesa = lastHamburguesa;
        papas = lastPapas;
        refresco = lastRefresco;
        helado = lastHelado;
        actualizarTextViews();
    }

    private void resetearPedido() {
        hamburguesa = papas = refresco = helado = 0;
        actualizarTextViews();
    }

    private void irAResumen() {
        Intent intent = new Intent(this, ResumenPedidoActivity.class);
        intent.putExtra("hamburguesa", hamburguesa);
        intent.putExtra("papas", papas);
        intent.putExtra("refresco", refresco);
        intent.putExtra("helado", helado);

        String pedido = "Tu pedido:\n\n";
        if (hamburguesa > 0) pedido += "🍔 Hamburguesa x" + hamburguesa + "\n";
        if (papas > 0)       pedido += "🍟 Papas x" + papas + "\n";
        if (refresco > 0)    pedido += "🥤 Refresco x" + refresco + "\n";
        if (helado > 0)      pedido += "🍦 Helado x" + helado + "\n";
        intent.putExtra("pedido_resumen", pedido);

        startActivityForResult(intent, REQUEST_CODE_RESUMEN);
    }

    // ==================== DIÁLOGOS MENÚ LATERAL ====================

    private void mostrarAyuda() {
        new AlertDialog.Builder(this)
                .setTitle("ℹ️ Ayuda")
                .setMessage("Fast Food Ordering System\n\n" +
                        "📱 Cómo usar la app:\n" +
                        "1. Selecciona productos con +/-\n" +
                        "2. Presiona el carrito para revisar\n" +
                        "3. Confirma o cancela tu pedido\n\n" +
                        "🔍 Búsqueda de productos:\n" +
                        "• Usa la lupa para buscar productos\n" +
                        "• Escribe el nombre del producto\n" +
                        "• Para volver a ver todo el menú,\n" +
                        "  selecciona Inicio en el menú lateral\n\n" +
                        "📋 Menú lateral:\n" +
                        "• Inicio: Reinicia tu pedido\n" +
                        "• Mis pedidos: Ver historial\n" +
                        "• Ayuda: Esta información\n\n" +
                        "🛒 Toolbar:\n" +
                        "• 🔍 Buscar pedidos\n" +
                        "• ⚙️ Configuración\n" +
                        "• ⋮ Más opciones")
                .setPositiveButton("Entendido", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void mostrarHistorialPedidos() {
        String historial = "📋 Últimos pedidos:\n\n";

        if (lastHamburguesa == 0 && lastPapas == 0 && lastRefresco == 0 && lastHelado == 0) {
            historial += "Aún no hay pedidos realizados";
        } else {
            historial += "🕐 Último pedido:\n";
            if (lastHamburguesa > 0) historial += "• 🍔 Hamburguesa x" + lastHamburguesa + "\n";
            if (lastPapas > 0)       historial += "• 🍟 Papas x" + lastPapas + "\n";
            if (lastRefresco > 0)    historial += "• 🥤 Refresco x" + lastRefresco + "\n";
            if (lastHelado > 0)      historial += "• 🍦 Helado x" + lastHelado + "\n";
        }

        new AlertDialog.Builder(this)
                .setTitle("Historial de Pedidos")
                .setMessage(historial)
                .setPositiveButton("Cerrar", null)
                .setNeutralButton("Repetir último", (dialog, which) -> {
                    restaurarUltimoPedido();
                    Toast.makeText(this, "Último pedido restaurado", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // ==================== TOOLBAR ====================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_buscar) {
            activarBusqueda();
            return true;

        } else if (id == R.id.action_configuración) {
            mostrarConfiguracion();
            return true;

        } else if (id == R.id.action_cancelar) {
            if (hamburguesa == 0 && papas == 0 && refresco == 0 && helado == 0) {
                Toast.makeText(this, "No hay pedido para cancelar", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Cancelar Pedido")
                        .setMessage("¿Estás seguro de cancelar el pedido actual?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Sí, cancelar", (dialog, which) -> {
                            resetearPedido();
                            Toast.makeText(this, "Pedido cancelado ❌", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            return true;

        } else if (id == R.id.action_reenviar) {
            if (lastHamburguesa == 0 && lastPapas == 0 && lastRefresco == 0 && lastHelado == 0) {
                Toast.makeText(this, "No hay pedidos anteriores", Toast.LENGTH_SHORT).show();
            } else {
                restaurarUltimoPedido();
                Toast.makeText(this, "Último pedido restaurado 🔄", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ==================== CONFIGURACIÓN ====================

    private void mostrarConfiguracion() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // ==================== RESULTADO DEL RESUMEN ====================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_RESUMEN) {
            if (resultCode == RESULT_OK && data != null) {
                boolean confirmado = data.getBooleanExtra("pedido_confirmado", false);
                if (confirmado) {
                    resetearPedido();
                    restaurarMenuCompleto();
                    Toast.makeText(this, "¡Pedido realizado con éxito! 🎉", Toast.LENGTH_LONG).show();
                    Log.d("MAIN", "Pedido confirmado - Contadores reseteados");
                }
            } else if (resultCode == RESULT_CANCELED && data != null) {
                hamburguesa = data.getIntExtra("hamburguesa", 0);
                papas       = data.getIntExtra("papas", 0);
                refresco    = data.getIntExtra("refresco", 0);
                helado      = data.getIntExtra("helado", 0);
                actualizarTextViews();
                Toast.makeText(this, "Pedido cancelado - Puedes modificarlo", Toast.LENGTH_SHORT).show();
                Log.d("MAIN", "Pedido cancelado - Datos recuperados");
            }
        }
    }
}