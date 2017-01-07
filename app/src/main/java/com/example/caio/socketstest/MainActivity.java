package com.example.caio.socketstest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnConexao;
    private TextView txvRetornoSocket;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvRetornoSocket = (TextView) findViewById(R.id.txvRetornoSocket);
        btnConexao = (Button) findViewById(R.id.btnConexao);
        btnConexao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v == btnConexao){
                    Toast.makeText(MainActivity.this, "Conectando...", Toast.LENGTH_SHORT).show();
                    ConexaoSocket cs = new ConexaoSocket();
                    cs.execute();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
    }

    private class ConexaoSocket extends AsyncTask<Void, String, Void>{

        @Override
        protected void onPreExecute(){
            Log.i("AsyncTask", "Exibindo ProgressDialog na tela Thread: "+Thread.currentThread().getName());
            load = ProgressDialog.show(MainActivity.this, "Por favor aguarde...", "Conectando...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Socket servidor = null;
            try {
                servidor = new Socket("192.168.0.20", 3232);
                try {
                    Scanner s = new Scanner(servidor.getInputStream());
                    while(s.hasNextLine()){
                        publishProgress(s.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    }
}

