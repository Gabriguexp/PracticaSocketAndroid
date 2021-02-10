package com.guibry.chatclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ChatFragment extends Fragment {

    Button btmsg, btvolver;
    TextView welcometv, tvChat, usuariosTv;
    private Thread hebraEscucha;
    private Socket cliente;
    private DataInputStream flujoE;
    private DataOutputStream flujoS;
    EditText mensajeEt;
    String user;
    NavController navController;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usuariosTv = view.findViewById(R.id.usuariosTv);
        navController = Navigation.findNavController(view);
        mensajeEt = view.findViewById(R.id.mensajeEt);
        user = InicioFragment.user;
        welcometv = view.findViewById(R.id.welcometv);
        welcometv.setText("Bienvenido " + user + "!");
        btvolver = view.findViewById(R.id.btvolver);
        tvChat = view.findViewById(R.id.tvChat);
        Thread hebra = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    cliente = new Socket("192.168.1.50", 5000);
                    flujoE = new DataInputStream(cliente.getInputStream());
                    flujoS = new DataOutputStream(cliente.getOutputStream());
                    hebraEscucha = new Thread() {
                        public void run() {
                            String text;
                            while (true) {
                                try {
                                    text = flujoE.readUTF();
                                    String finalText = text;
                                    if (text.startsWith("[codigo:__001]:")) {
                                        String replace = text.replace("[codigo:__001]:", "Usuarios conectados: \n");
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                usuariosTv.setText(replace);
                                            }
                                        });

                                    } else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvChat.append(finalText + "\n");
                                            }
                                        });
                                    }
                                } catch (IOException ex) {
                                    System.out.println("Run: " + ex.getLocalizedMessage());
                                }
                            }
                        }
                    };
                    hebraEscucha.start();
                } catch (IOException ex) {
                    Log.v("xyz", ex.getLocalizedMessage());
                }
            }
        });

        hebra.start();
        btmsg = view.findViewById(R.id.btenviar);
        btmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String text = mensajeEt.getText().toString();
                        try {
                            flujoS.writeUTF(text);
                            //text = flujoE.readUTF();
                            //taText.append(text+"\n");

                        } catch (IOException ex) {
                            System.out.println("send msg: " + ex.getLocalizedMessage());
                        }

                    }
                });
                thread.start();
                mensajeEt.setText("");
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startClient();
        btvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_chatFragment_to_inicioFragment);

            Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    flujoS.writeUTF("[codigo:__002]:");
                } catch (IOException e) {
                    Log.v("xyzstop", e.getLocalizedMessage());
                }
            }
        });
        thread.start();


            }
        });
    }


    public void startClient() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    flujoS.writeUTF(user);
                } catch (IOException ex) {
                    System.out.println("send msg: " + ex.getLocalizedMessage());
                }

            }
        });
        thread.start();
    }
}