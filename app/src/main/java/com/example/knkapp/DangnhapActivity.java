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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DangnhapActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =100 ;
    GoogleSignInClient mGoogleSignInClient;
    // khai báo các biến
    EditText editEmail, editPassword;
    TextView txtNotaccount, txtQuenmatkhau;
    Button btnDangnhap;
    SignInButton btnDNgoogle;

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

        // trước mauth
        // xác định cấu hình đăng nhập của google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        //Trong phương thức onCreate (), khởi tạo thể hiện FirebaseAuth.
        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // gọi id từ .xml
        editEmail= findViewById(R.id.Edit_DNemail_id);
        editPassword= findViewById(R.id.Edit_DNpassword_id);
        txtNotaccount= findViewById(R.id.txt_DNnoaccount_id);
        btnDangnhap= findViewById(R.id.btn_DNdangnhap_id);
        txtQuenmatkhau= findViewById(R.id.txt_DNquenmk_id);
        btnDNgoogle= findViewById(R.id.btn_DNgoogle_id);


        // xử lý xự kiện đăng nhập bằng tài khoản google
        btnDNgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bắt đầu đăng nhập sử dụng tài khoản google
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

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
        builder.setTitle("Nhập gmail để lấy lại mật khẩu");
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
        builder.setPositiveButton("Gửi yêu cầu", new DialogInterface.OnClickListener() {
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
        progressDialog.setMessage("Đang gửi thông tin đến Email...");
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
                Toast.makeText(DangnhapActivity.this, "Gmail không chính xác, xảy ra lỗi !", Toast.LENGTH_SHORT).show();
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
                            startActivity(new Intent(DangnhapActivity.this, BangDieuKhienActivity.class));
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
// hàm bật hoạt động của tài khoản google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Lỗi khi đăng nhập bằng tài khoản google", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // hàm đăng nhập bằng tài khoản google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Đăng nhập thành công, cập nhật giao diện người dùng
                            // với các đăng nhập thông tin của người dùng
                            FirebaseUser user = mAuth.getCurrentUser();

                            // nếu người dùng đã đăng nhập, sẽ lây và hiển thị thông tin người dùng
                            if (task.getResult().getAdditionalUserInfo().isNewUser()){

                                // Lấy email người dùng và id của auth
                                String email= user.getEmail();
                                String uid= user.getUid();
                                //Khi người dùng đâng kí, thông tin người dùng được lưu trữ theo thời gian thực
                                // sử dụng hashMap
                                HashMap<Object,String> hashMap= new HashMap<>();
                                // đưa thông tin vào HashMap
                                hashMap.put("email",email);// email người dùng
                                hashMap.put("uid",uid);// id người dùng
                                hashMap.put("name","");// tên người dùng
                                hashMap.put("phone","");// người dùng
                                // cơ sở dữ liệu của firebase
                                FirebaseDatabase firebaseDatabase=  FirebaseDatabase.getInstance();
                                // đường dẫn lưu trữ dữ liệu người dùng có tên "Users"
                                DatabaseReference reference= firebaseDatabase.getReference("Users");
                                // đưa dữ liệu vào hàm Hashmap trong csdl
                                reference.child(uid).setValue(hashMap);

                            }

                            // hiển thị email người dùng bằng Toast
                            Toast.makeText(DangnhapActivity.this, "Đã kết nối tới tài khoản Google "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            // người dùng đăng nhập thành công và mở activity Hồ sơ
                            startActivity(new Intent(DangnhapActivity.this, BangDieuKhienActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            //Nếu đăng nhập thất bại, hiển thị một thông báo cho người dùng.
                            // hiển thị thông báo lỗi
                            Toast.makeText(DangnhapActivity.this, "Lỗi đăng nhập...", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // lấy và hiển thị lỗi. ( project of Tran dang khoa )
                Toast.makeText(DangnhapActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
