package com.example.caio.socketstest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnConexao;
    private TextView txvRetornoSocket;
    private Socket servidor;

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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            conectarSocket();
                        }
                    }).start();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
    }

    private void conectarSocket() {
        try {
            try {
                txvRetornoSocket.setText("Conectando...");
                servidor = new Socket("192.168.0.20", 3232);
                txvRetornoSocket.setText("Socket iniciado!!");
            }catch (IOException e){
                e.printStackTrace();
            }

            try {
                InputStream dadosServidor = servidor.getInputStream();
                Scanner s = new Scanner(dadosServidor);
                while(s.hasNextLine()){
                    txvRetornoSocket.setText(s.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro: "+e.getMessage());
        }
    }
}
