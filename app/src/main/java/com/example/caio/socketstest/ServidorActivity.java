package com.example.caio.socketstest;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Scanner;

public class ServidorActivity extends AppCompatActivity{

    private Button btnIniciarServidor;
    private Button btnEnviar;
    private TextView txvStatusConexao;
    private TextView txvRespostaCliente;
    private EditText editTextMsgCliente;
    private IniciarServidorSocket iss;

    private View.OnClickListener iniciarServidorOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iss.execute();
        }
    };

    private View.OnClickListener enviarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = editTextMsgCliente.getText().toString();
            iss.enviarParaCliente(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);

        txvRespostaCliente = (TextView) findViewById(R.id.txvRespostaCliente);
        txvRespostaCliente.setText("Nenhum cliente conectado...");

        txvStatusConexao = (TextView) findViewById(R.id.txvStatusConexao);

        editTextMsgCliente = (EditText) findViewById(R.id.editTextMsgCliente);
        editTextMsgCliente.setVisibility(View.INVISIBLE);

        iss = new IniciarServidorSocket(15353);

        btnIniciarServidor = (Button) findViewById(R.id.btnIniciarServidor);
        btnIniciarServidor.setOnClickListener(iniciarServidorOnClickListener);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviar.setVisibility(View.INVISIBLE);
        btnEnviar.setClickable(false);
        btnEnviar.setEnabled(false);
        btnEnviar.setOnClickListener(enviarOnClickListener);


    }

    private class IniciarServidorSocket extends AsyncTask<String, String, Void>{

        private PrintStream saidaCliente;
        private int porta;
        private String ipAparelho;

        public IniciarServidorSocket(int porta){
            this.porta = porta;
            this.ipAparelho = wifiIpAddress(ServidorActivity.this);
        }

        @Override
        protected void onPreExecute(){
            btnIniciarServidor.setClickable(false);
            btnIniciarServidor.setEnabled(false);
        }

        @Override
        protected Void doInBackground(String... args) {
            try{
                ServerSocket servidor = new ServerSocket(this.porta);
                publishProgress("Esperando cliente...", "Servidor iniciado \nIp: "+this.ipAparelho+" \nPorta: "+this.porta);
                Socket cliente = servidor.accept();
                publishProgress("Esperando o cliente conectado...", "Cliente conectado\nip: "+ cliente.getInetAddress().getHostAddress(), "true");

                this.saidaCliente = new PrintStream(cliente.getOutputStream());
                this.saidaCliente.println("Vc foi conectado!");

                Scanner s = new Scanner(cliente.getInputStream());
                while (s.hasNextLine()) {
                    publishProgress(s.nextLine());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            txvStatusConexao.setText("Conexão encerrada!");
        }

        @Override
        protected void onProgressUpdate(String... update) {
            int tam;
            if((tam = update.length) > 0) {
                if(tam >= 3) {
                    if (update[2].equals("true")) {
                        editTextMsgCliente.setVisibility(View.VISIBLE);

                        btnEnviar.setVisibility(View.VISIBLE);
                        btnEnviar.setClickable(true);
                        btnEnviar.setEnabled(true);
                    }
                }

                if (tam == 1) {
                    txvRespostaCliente.setText(update[0]);
                }else if(tam >= 2){
                    txvRespostaCliente.setText(update[0]);
                    txvStatusConexao.setText(update[1]);
                }
            }
        }

        public void enviarParaCliente(String s){
            editTextMsgCliente.setText("");
            this.saidaCliente.println(s);
        }

        /*
         * Método para pegar IP do aparelho
         */
        protected String wifiIpAddress(Context context) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

            // Convert little-endian to big-endianif needed
            if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
                ipAddress = Integer.reverseBytes(ipAddress);
            }

            byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

            String ipAddressString;
            try {
                ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
            } catch (UnknownHostException ex) {
                Log.e("WIFIIP", "Unable to get host address.");
                ipAddressString = null;
            }

            return ipAddressString;
        }

    }

}
