package com.example.knkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BangDieuKhienActivity extends AppCompatActivity {

    // người dùng
    FirebaseAuth firebaseAuth;
    // khởi tạo biến
    //TextView txtHoso;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangdieukhien);

        // tạo thanh tiêu đề dùng actionbar
         actionBar = getSupportActionBar();
        actionBar.setTitle("Hồ sơ người dùng");

        // lấy người dùng
        firebaseAuth = FirebaseAuth.getInstance();
        //lấy id của .xml
       // txtHoso= findViewById(R.id.txt_HSHoso_id);
        // lấy id của Bottomnavigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.Bottom_BDK_id);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);


        actionBar.setTitle("Trang chủ"); // thay đổi tiêu đề thanh trạng thái
        TrangchuFragment trangchufragment= new TrangchuFragment();
        FragmentTransaction Ft1= getSupportFragmentManager().beginTransaction();
        Ft1.replace(R.id.Frame_BDK_id,trangchufragment,"");
        Ft1.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    // xử lý danh mục khi ấn click
                    switch (menuItem.getItemId()){

                        case R.id.nav_hoso:
                            actionBar.setTitle("Hồ sơ"); // thay đổi tiêu đề thanh trạng thái
                            HosoFragment hosoFragment= new HosoFragment();
                            FragmentTransaction Ft2= getSupportFragmentManager().beginTransaction();
                            Ft2.replace(R.id.Frame_BDK_id,hosoFragment,"");
                            Ft2.commit();
                            return true;

                        //
                        case R.id.nav_banbe:
                            actionBar.setTitle("Bạn bè"); // thay đổi tiêu đề thanh trạng thái
                            BanbeFragment banbeFragment= new BanbeFragment();
                            FragmentTransaction Ft3= getSupportFragmentManager().beginTransaction();
                            Ft3.replace(R.id.Frame_BDK_id,banbeFragment,"");
                            Ft3.commit();
                            return true;
                    }
                    return false;
                }
            };


    private void checkUserStatus(){
        // lấy người dùng hiện tại
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user != null){
            // người dùng đã đăng nhâp ở đây
            // lấy email đăng nhập của người dùng
            //txtHoso.setText(user.getEmail());
        } else {
            // người dùng chưa đăng nhập, đi đến main activity
            startActivity(new Intent(BangDieuKhienActivity.this, MainActivity.class));
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
