package com.example.knkapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HosoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoso);

        // tạo thanh tiêu đề dùng actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Hồ sơ người dùng");
    }
}
