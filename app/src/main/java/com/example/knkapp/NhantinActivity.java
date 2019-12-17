package com.example.knkapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class NhantinActivity extends AppCompatActivity {

    // khai báo
    Toolbar toolbar;
    RecyclerView recyclerView; // danh sách views
    ImageView imV_anhNguoiNhanTin; // nút gửi tin nhắn
    TextView  txtTenNguoiNhanTinNhan, txtTinhTrangNguoiNhanTin; // tên người gửi và Tình trạng
    EditText editSoanTinNhan;// soạn tin nhắn
    ImageButton BtnGuiTinNhan; // bút gửi tin nhắn



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhantin);
        //gọi id từ views xml
        Toolbar toolbar = (Toolbar) findViewById(R.id.thanhtt_id);
        //setSupportActionBar(toolbar); lỗi
        toolbar.setTitle(" ");

        recyclerView= findViewById(R.id.DSNhantin_recyclerView);
        imV_anhNguoiNhanTin= findViewById(R.id.anhnguoinhantin_id);
        txtTenNguoiNhanTinNhan= findViewById(R.id.txtNguoinhan_id);
        txtTinhTrangNguoiNhanTin= findViewById(R.id.txtTinhTrangNguoiNhan_id);
        BtnGuiTinNhan= findViewById(R.id.Ibtn_guiTinNhan_id);
        editSoanTinNhan= findViewById(R.id.txt_SoanTinNhan_id);
    }
}
