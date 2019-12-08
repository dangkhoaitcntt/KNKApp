package com.example.knkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DangnhapActivity extends AppCompatActivity {

    // khai báo các biến
    EditText editEmail, editPassword;
    TextView txtNotaccount, txtQuenmatkhau;
    Button btnDangnhap;

    //Khai báo một thể hiện của FirebaseAuth
    private FirebaseAuth mAuth;
    // khai báo progress dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);


        // tạo thanh tiêu đề dùng actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Đăng nhập tài khoản");

        // bật nút quay lại
        actionBar.setDisplayHomeAsUpEnabled(true); // hiển thị trang chủ
        actionBar.setDisplayShowCustomEnabled(true); // hiển thị chế độ tùy chỉnh

        //Trong phương thức onCreate (), khởi tạo thể hiện FirebaseAuth.
        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // gọi id từ .xml
        editEmail= findViewById(R.id.Edit_DNemail_id);
        editPassword= findViewById(R.id.Edit_DNpassword_id);
        txtNotaccount= findViewById(R.id.txt_DNnoaccount_id);
        btnDangnhap= findViewById(R.id.btn_DNdangnhap_id);
        txtQuenmatkhau= findViewById(R.id.txt_DNquenmk_id);

        // xử lý nút button đăng nhập khi click
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nhập dữ liệu từ account và mật khẩu
                String email= editEmail.getText().toString();
                String password= editPassword.getText().toString().trim(); // trim() là che mật khẩu
                // kiểm tra email có hợp lệ hay không !
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    // nếu email không hợp lệ
                    editEmail.setError("Email không đúng");
                    editEmail.setFocusable(true);
                }else{
                    // nếu đúng email
                    DangnhapNguoiDung(email, password);
                }
            }
        });
        // xử lý khi click vào không có tài khoản
        txtNotaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gọi đến activity đăng kí
                startActivity(new Intent(DangnhapActivity.this, DangkiActivity.class));
                finish();
            }
        });

        // xử lý sự kiện click quên mật khẩu
        txtQuenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hopthoailaylaimk();
            }
        });

        //hộp thoại tiến trình
        progressDialog = new ProgressDialog(this);

    }
    // hàm lấy lại mật khẩu
    private void hopthoailaylaimk() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Lấy lại mật khẩu");
        //thiết lập liner layout
        LinearLayout linearLayout= new LinearLayout(this);
        // tạo Edit text
        final EditText editLayMK= new EditText(this);
        editEmail.setHint("Email");
        editLayMK.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        editLayMK.setMinEms(16);

        linearLayout.addView(editLayMK);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        // tạo Button nhận lại mật khẩu
        builder.setPositiveButton("Nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nhập email
                String email=  editLayMK.getText().toString().trim(); // trim() che mật khẩu
                batdaulayMK(email);
            }
        });

        // tạo Button thoát
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // thoát khổi tiến trình
                dialog.dismiss();
            }
        });
        // show tiến trình
        builder.create().show();

    }
    // hàm bắt đầu lấy mật khẩu
    private void batdaulayMK(String email) {
        // show hộp thoại progress dialog
        progressDialog.setMessage("Đang gửi thông tin đến Email..");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(DangnhapActivity.this, "Kiểm tra email !", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DangnhapActivity.this, "Lỗi...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                // hiện thị lỗi
                Toast.makeText(DangnhapActivity.this, "Đã xảy ra lỗi !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Tạo một phương thức đăng nhập mới lấy địa chỉ email và mật khẩu,
    // xác thực chúng và sau đó đăng nhập người dùng bằng phương thức signInWithEmailAndPassword.
    private void DangnhapNguoiDung(String email, String password) {
        // show hộp thoại progress dialog
        progressDialog.setMessage("Đang đăng nhập..");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // tắt hộp thoại progress dialog
                            progressDialog.dismiss();
                            // Đăng nhập thành công, cập nhật giao diện người dùng
                            // với thông tin người dùng đã đăng nhập
                            FirebaseUser user = mAuth.getCurrentUser();
                            // người dùng đăng nhập thành công và mở activity Hồ sơ
                            startActivity(new Intent(DangnhapActivity.this,HosoActivity.class));
                            finish();
                        } else {
                            // tắt hộp thoại progress dialog
                            progressDialog.dismiss();
                            // Nếu đăng nhập thất bại, hiển thị thông báo cho người dùng.
                            Toast.makeText(DangnhapActivity.this, "Đăng nhập thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // tắt hộp thoại progress dialog
                progressDialog.dismiss();
                // lỗi, và thông báo lỗi bằng thông báo
                Toast.makeText(DangnhapActivity.this, "Vui lòng kiểm tra lại thông tin !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // đi đến activity trước
        return super.onSupportNavigateUp();
    }
}
