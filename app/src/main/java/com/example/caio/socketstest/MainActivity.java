package com.example.caio.socketstest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnConexao;
    private Button btnEnviar;
    private TextView txvRetornoSocket;
    private ProgressDialog load;
    private EditText editText, editTextIpServidor, editTextPortaServidor;
    private ConexaoSocket cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        editTextIpServidor = (EditText) findViewById(R.id.editTextIpServidor);
        editTextPortaServidor = (EditText) findViewById(R.id.editTextPortaServidor);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cs.enviarParaServidor(editText.getText().toString());
            }
        });

        btnEnviar.setClickable(false);
        btnEnviar.setEnabled(false);

        txvRetornoSocket = (TextView) findViewById(R.id.txvRetornoSocket);
        btnConexao = (Button) findViewById(R.id.btnConexao);
        btnConexao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ip;
                int porta;
                if ((ip = editTextIpServidor.getText().toString()).length() >= 7 && (porta = Integer.parseInt(editTextPortaServidor.getText().toString())) > 0){
                    cs = new ConexaoSocket(ip, porta);
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
        private int portaServidor;
        private String ipServidor;

        public ConexaoSocket(String ipServidor, int portaServidor){
            this.ipServidor = ipServidor;
            this.portaServidor = portaServidor;
        }

        @Override
        protected void onPreExecute(){
            btnConexao.setClickable(false);
            btnConexao.setEnabled(false);

            editTextIpServidor.setEnabled(false);
            editTextPortaServidor.setEnabled(false);

            load = ProgressDialog.show(MainActivity.this,"Conctando...","Aguarde...");
        }

        @Override
        protected Void doInBackground(String... args) {
            Socket servidor;
            try {
                servidor = new Socket(this.ipServidor, this.portaServidor);
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

                if(!btnEnviar.isClickable() || !btnEnviar.isEnabled()) {
                    btnEnviar.setClickable(true);
                    btnEnviar.setEnabled(true);
                }

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
            editText.setText("");
            saidaServidor.println(s);
        }
    }
}

