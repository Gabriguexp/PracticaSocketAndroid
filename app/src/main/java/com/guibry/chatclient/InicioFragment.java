package com.guibry.chatclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InicioFragment extends Fragment {

    EditText nombreEt;
    Button btinicio;
    public static String user;
    NavController navController;
    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        btinicio = view.findViewById(R.id.btinicio);
        nombreEt = view.findViewById(R.id.etusuario);

        btinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = nombreEt.getText().toString();
                if(user.isEmpty()){
                    Toast.makeText(getContext(), "El nombre no puede estar vacio", Toast.LENGTH_LONG).show();
                } else{
                    navController.navigate(R.id.action_inicioFragment_to_chatFragment);
                }
            }
        });

    }
}