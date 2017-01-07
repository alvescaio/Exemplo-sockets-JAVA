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
                    conectarSocket();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
    }

    private void conectarSocket() {
        try {
            txvRetornoSocket.setText("Conectando...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        servidor = new Socket("192.168.0.20", 3232);
                    } catch (IOException e) {
                        txvRetornoSocket.setText("Exception 1: "+e.getMessage());
                        e.printStackTrace();
                    } finally {
                        try {
                            servidor.close();
                        } catch (IOException e) {
                            txvRetornoSocket.setText("Exception 2: "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            });

            txvRetornoSocket.setText("Socket iniciado!!");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream dadosServidor = servidor.getInputStream();
                        Scanner s = new Scanner(dadosServidor);
                        while(s.hasNextLine()){
                            txvRetornoSocket.setText(s.nextLine());
                        }
                    } catch (IOException e) {
                        txvRetornoSocket.setText("Exception 4: "+e.getMessage());
                        e.printStackTrace();
                    }

                }
            }).start();

        }catch (Exception e){
            txvRetornoSocket.setText("Exception 3: "+e.getMessage());
            e.printStackTrace();
            System.out.println("Erro: "+e.getMessage());
        }
    }
}
