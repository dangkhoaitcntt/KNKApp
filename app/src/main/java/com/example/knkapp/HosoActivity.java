package com.example.knkapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HosoActivity extends AppCompatActivity {

    // người dùng
    FirebaseAuth firebaseAuth;
    // khởi tạo biến
    TextView txtHoso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoso);

        // tạo thanh tiêu đề dùng actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Hồ sơ người dùng");

        // lấy người dùng
        firebaseAuth = FirebaseAuth.getInstance();
        //lấy id của .xml
        txtHoso= findViewById(R.id.txt_HSHoso_id);
    }

    private void checkUserStatus(){
        // lấy người dùng hiện tại
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user != null){
            // người dùng đã đăng nhâp ở đây
            // lấy email đăng nhập của người dùng
            txtHoso.setText(user.getEmail());
        } else {
            // người dùng chưa đăng nhập, đi đến main activity
            startActivity(new Intent(HosoActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        // kiểm tra khi bắt đầu ứng dụng
        checkUserStatus();
        super.onStart();
    }
    /*tùy chon menu*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu nâng cao dùng infale
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // xử lý khi click vào mục của menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // lấy id của các mục
        int id= item.getItemId();
        if(id== R.id.action_dangxuat){
            firebaseAuth.signOut();// đăng xuất ra khổi tài khoản
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}
