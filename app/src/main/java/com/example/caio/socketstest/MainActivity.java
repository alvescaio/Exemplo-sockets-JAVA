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

    private class ConexaoSocket extends AsyncTask<Void, Void, Socket>{

        @Override
        protected void onPreExecute(){
            Log.i("AsyncTask", "Exibindo ProgressDialog na tela Thread: "+Thread.currentThread().getName());
            load = ProgressDialog.show(MainActivity.this, "Por favor aguarde...", "Conectando...");
        }

        @Override
        protected Socket doInBackground(Void... voids) {
            Socket servidor = null;
            try {
                servidor = new Socket("192.168.0.20", 3232);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Erro: "+e.getMessage());
            }
            return servidor;
        }

        @Override
        protected void onPostExecute(Socket servidor){
            if(servidor != null){
                try {
                    Scanner s = new Scanner(servidor.getInputStream());
                    while(s.hasNextLine()){
                        txvRetornoSocket.setText(s.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

