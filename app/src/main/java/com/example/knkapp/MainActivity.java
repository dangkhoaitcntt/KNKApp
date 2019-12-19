package com.example.knkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    // khởi tạo 2 biến button
    Button btnDangnhap, btnDangki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // gọi id từ view
        btnDangki = findViewById(R.id.btn_Mdangki_id);
        btnDangnhap= findViewById(R.id.btn_Mdangnhap_id);
        // xử lý click của button đăng kí tài khoản người dùng
        btnDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi động activity đăng ký
                startActivity(new Intent(MainActivity.this, DangkiActivity.class));
            }
        });

        // xử lý nút button đăng nhập khi click vào. (bản quyền thuộc Trần Đăng Khoa)
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gọi activity đăng nhập
                startActivity(new Intent(MainActivity.this, DangnhapActivity.class));
            }
        });
    }
}
