package com.example.geomobileapp;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;


    public MainAdapter(Activity context, List<MainData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.nom.setText(data.getNom());
        holder.prenom.setText(data.getPrenom());
        holder.tel.setText(data.getTel());
        holder.email.setText(data.getEmail());
        holder.dateNais.setText(data.getDateNais());

        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainData d = dataList.get(holder.getAdapterPosition());
                int sID = d.getID();
                String sText = d.getNom();
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                dialog.show();

                EditText nom = dialog.findViewById(R.id.nom);
                /*EditText prenom = dialog.findViewById(R.id.prenom);
                EditText tel = dialog.findViewById(R.id.tel);
                EditText dateNais = dialog.findViewById(R.id.dateNaissance);
                EditText email = dialog.findViewById(R.id.email);*/
                Button btn_update = dialog.findViewById(R.id.btn_edit);

                nom.setText(sText);
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String uText = nom.getText().toString().trim();
                        database.mainDao().update(sID, uText);
                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        notifyDataSetChanged();
                    }
                });
            }
        });

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData d = dataList.get(holder.getAdapterPosition());
                database.mainDao().delete(d);
                int position = holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(position, dataList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nom, prenom, email, tel, dateNais;
        ImageView btEdit, btDelete;

        public ViewHolder(@NonNull View view) {
            super(view);
            nom = view.findViewById(R.id.nom);
            prenom = view.findViewById(R.id.prenom);
            tel = view.findViewById(R.id.tel);
            email = view.findViewById(R.id.email);
            dateNais = view.findViewById(R.id.dateNaissance);
            btEdit = view.findViewById(R.id.btn_edit);
            btDelete = view.findViewById(R.id.btn_delete);
        }
    }
}