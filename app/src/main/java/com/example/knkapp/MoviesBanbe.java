package com.example.knkapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Tạo Custom Adapter và bind dữ liệu cho từng row trong Adapter

public class MoviesBanbe extends RecyclerView.Adapter<MoviesBanbe.MyHolder>{

    Context context;
    List<ModelBanBe> userList;

    public MoviesBanbe(Context context, List<ModelBanBe> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_users, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String TenBanBe= userList.get(position).getName();
        final String EmailBanBe= userList.get(position).getEmail();

        holder.tenBanBe.setText(TenBanBe);
        holder.emailBeBe.setText(EmailBanBe);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class  MyHolder extends RecyclerView.ViewHolder{

       TextView tenBanBe, emailBeBe;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tenBanBe= itemView.findViewById(R.id.ten_id_banbe);
            emailBeBe= itemView.findViewById(R.id.email_id_banbe);
        }
    }
}
