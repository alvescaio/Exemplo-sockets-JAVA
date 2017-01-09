package com.example.caio.socketstest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LauncherActivity extends Activity {

    private Button btnServidor, btnCiente;

    private View.OnClickListener servidorOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(LauncherActivity.this,
                    ServidorActivity.class);

            startActivity(intent);

            finish();
        }
    };

    private View.OnClickListener clienteOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(LauncherActivity.this,
                    MainActivity.class);

            startActivity(intent);

            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        btnCiente = (Button) findViewById(R.id.btnCliente);
        btnCiente.setOnClickListener(clienteOnclick);
        btnServidor = (Button) findViewById(R.id.btnServidor);
        btnServidor.setOnClickListener(servidorOnclick);

    }
}
