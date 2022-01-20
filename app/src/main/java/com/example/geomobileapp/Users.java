package com.example.geomobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Users extends AppCompatActivity {
    EditText prenom, tel, dateNais, email;
    EditText nom;
    Button btn_add, btn_reset;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        tel = findViewById(R.id.tel);
        dateNais = findViewById(R.id.dateNaissance);
        email = findViewById(R.id.email);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        recyclerView = findViewById(R.id.recycler_view);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MainAdapter(Users.this, dataList);

        recyclerView.setAdapter(adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String last_name = nom.getText().toString().trim();
                String first_name = prenom.getText().toString().trim();
                String telephone = tel.getText().toString().trim();
                String date_naissance = dateNais.getText().toString().trim();
                String e_mail = email.getText().toString().trim();
                MainData data = new MainData();
                data.setNom(last_name);
                data.setPrenom(first_name);
                data.setTel(telephone);
                data.setDateNais(date_naissance);
                data.setEmail(e_mail);
                database.mainDao().insert(data);
                nom.setText("");
                prenom.setText("");
                tel.setText("");
                email.setText("");
                dateNais.setText("");
                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.mainDao().reset(dataList);
                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
            }
        });
    }
}