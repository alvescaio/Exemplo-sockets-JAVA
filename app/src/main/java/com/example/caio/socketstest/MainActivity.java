package com.example.caio.socketstest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnConexao;
    private Button btnEnviar;
    private TextView txvRetornoSocket;
    private ProgressDialog load;
    private EditText editText;
    private ConexaoSocket cs = new ConexaoSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == btnEnviar){
                    cs.enviarParaServidor(editText.getText().toString());
                }
            }
        });

        btnEnviar.setClickable(false);
        btnEnviar.setEnabled(false);

        txvRetornoSocket = (TextView) findViewById(R.id.txvRetornoSocket);
        btnConexao = (Button) findViewById(R.id.btnConexao);
        btnConexao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v == btnConexao){
                    Toast.makeText(MainActivity.this, "Conectando...", Toast.LENGTH_SHORT).show();
                    cs.execute();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
    }

    private class ConexaoSocket extends AsyncTask<String, String, Void>{

        private PrintStream saidaServidor;

        @Override
        protected void onPreExecute(){
            Log.i("AsyncTask", "Exibindo ProgressDialog na tela Thread: "+Thread.currentThread().getName());
            btnConexao.setClickable(false);
            btnConexao.setEnabled(false);
            btnEnviar.setClickable(true);
            btnEnviar.setEnabled(true);
            editText.setText("");
            load = ProgressDialog.show(MainActivity.this, "Por favor aguarde...", "Conectando...");

        }

        @Override
        protected Void doInBackground(String... args) {
            Socket servidor;
            try {
                servidor = new Socket("192.168.0.20", 3232);
                try {
                    saidaServidor = new PrintStream(servidor.getOutputStream());
                    saidaServidor.println("Fui conectado!");

                    Scanner s = new Scanner(servidor.getInputStream());

                    while (s.hasNextLine()) {
                        publishProgress(s.nextLine());
                    }
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                servidor.close();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Erro: "+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... s) {
            if(s.length > 0){
                if(load.isShowing()){
                    load.dismiss();
                }
                txvRetornoSocket.setText(s[0]);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(load.isShowing()){
                load.dismiss();
            }
        }

        public void enviarParaServidor(String s){
            saidaServidor.println(s);
        }
    }
}

