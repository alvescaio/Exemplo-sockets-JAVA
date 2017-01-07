package com.example.caio.socketstest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnConexao;
    private TextView txvRetornoSocket;

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
            Socket socket = null;
            ObjectOutputStream canalSaida = null;
            ObjectInputStream canalEntrada = null;

            System.out.println("Iniciando socket na porta 3232...");
            socket = new Socket("192.168.0.20", 3232);

            System.out.println("Socket iniciado!!");
            canalSaida = new ObjectOutputStream(socket.getOutputStream());
            canalSaida.writeObject("Teste");

            canalEntrada = new ObjectInputStream(socket.getInputStream());
            Object obj = canalEntrada.readObject();
            if((obj != null) && (obj instanceof String)){
                txvRetornoSocket.setText(obj.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro: "+e.getMessage());
        }
    }
}
