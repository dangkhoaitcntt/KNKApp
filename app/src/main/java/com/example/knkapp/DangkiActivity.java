package com.example.knkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DangkiActivity extends AppCompatActivity {
    // khai báo button và edittext
    EditText EditEmail, EditPassword;
    Button btnDangki;
    TextView txtYesaccount;

    // khai báo sử dụng hộp thoại
    ProgressDialog progressDialog ;
    //Khai báo một thể hiện của Firebase Auth
    private FirebaseAuth mAuth;

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
        EditEmail = findViewById(R.id.Edit_Demail_id);
        EditPassword= findViewById(R.id.Edit_Dpassword_id);
        btnDangki = findViewById(R.id.btn_Ddangki_id);
        txtYesaccount= findViewById(R.id.txt_Dyesaccount_id);

        //Trong phương thức onCreate (), khởi tạo thể hiện FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đăng kí người dùng...");

        btnDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nhập tài khoản , mật khẩu
                String email= EditEmail.getText().toString().trim();
                String password = EditPassword.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    // set lỗi
                    EditEmail.setError("Email không hợp lệ ");
                    EditEmail.setFocusable(true);
                }else if(password.length()<6){
                    EditPassword.setError("Mật khẩu phải dài hơn 6 kí tự ");
                    EditPassword.setFocusable(true);
                }else {
                    registerUser(email,password);
                }

            }
        });
        // xử lý sự kiện nếu đã có tài khoản, chuyển đến trang đăng nhập
        txtYesaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangkiActivity.this,DangnhapActivity.class));
                finish();
            }
        });
    }

    private void registerUser(String email, String password) {
    // tài khoản và mật khẩu hợp lệ, bắt đầu đăng nhập use
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success.
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Lấy email người dùng và id của auth
                            String email= user.getEmail();
                            String uid= user.getUid();
                            //Khi người dùng đâng kí, thông tin người dùng được lưu trữ theo thời gian thực
                            // sử dụng hashMap
                            HashMap<Object, String> hashMap= new HashMap<>();
                            // đưa thông tin vào HashMap
                            hashMap.put("email",email); // email người dùng
                            hashMap.put("uid",uid);  // id người dùng
                            hashMap.put("name","");// sẽ thêm sao
                            hashMap.put("phone",""); // sdt người dùng

                            // cơ sở dữ liệu của firebase
                            FirebaseDatabase firebaseDatabase=  FirebaseDatabase.getInstance();

                            // đường dẫn lưu trữ dữ liệu người dùng có tên "Users"
                            DatabaseReference reference= firebaseDatabase.getReference("Users");
                            // đưa dữ liệu vào hàm Hashmap trong csdl
                            reference.child(uid).setValue(hashMap);


                            Toast.makeText(DangkiActivity.this, "Đăng kí tài khoản "+user.getEmail()+" thành công !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DangkiActivity.this, BangDieuKhienActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(DangkiActivity.this, "Lỗi tạo tài khoản !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // lỗi , bỏ qua tiến trình và hiển thị hộp thoại lỗi (project này của Trần Đăng khoa)
                progressDialog.dismiss();
                Toast.makeText(DangkiActivity.this," Địa chỉ gmail "+EditEmail.getText().toString()+" đã có người sử dụng !"/*+e.getMessage()*/,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // đi đến activity trước
        return super.onSupportNavigateUp();
    }
}
