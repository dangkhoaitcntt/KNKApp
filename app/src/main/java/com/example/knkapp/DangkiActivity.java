package com.example.knkapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class DangkiActivity extends AppCompatActivity {
    // khai báo button và edittext
    EditText EditEmail, EditPassword;
    Button btnDangki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);
        // tạo thanh tiêu đề dùng actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tạo tài khoản");
        // bật nút quay lại
        actionBar.setDisplayHomeAsUpEnabled(true); // hiển thị trang chủ
        actionBar.setDisplayShowCustomEnabled(true); // hiển thị chế độ tùy chỉnh

        // gọi id từ view

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // đi đến activity trước
        return super.onSupportNavigateUp();
    }
}
