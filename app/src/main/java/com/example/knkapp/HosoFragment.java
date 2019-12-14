package com.example.knkapp;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.PipedInputStream;
import java.security.Key;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class HosoFragment extends Fragment {

    // khai báo lưu trữ fireBase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    // khai báo biến hình ảnh và text
    TextView txtHotenid, txtGmailid, txtSDTid;
    // gọi nút button edit thông tin người dùng
    FloatingActionButton ButtonEditInfo;
    // khai tạo tên hộp thoại là progressDialog (hộp thoại hiển thị)
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hoso, container, false);

        // gọi firebase
        firebaseAuth= FirebaseAuth.getInstance();
        user= firebaseAuth.getCurrentUser();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("Users");
        // gọi id từ xml
        txtHotenid= view.findViewById(R.id.txt_HShoten_id);
        txtGmailid= view.findViewById(R.id.txt_HSgmail_id);
        txtSDTid= view.findViewById(R.id.txt_HSsdt_id);
        ButtonEditInfo = view.findViewById(R.id.btn_chinhsua_id);

        //tạo hộp thoại
        progressDialog = new ProgressDialog(getActivity());

        // chúng ta cần có được thông tin hiện tại của người dùng đang đăng nhập
        // để có được nó chúng ta cần có id hoặc gmail của user
        // ở đây khoa sẽ lấy người dùng bằng email lúc đã đăng ký
        // sử dụng truy vấn orderbyChild để lấy thông tin người dùng dựa trên email

        Query query= databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // kiểm tra cho đến khi nhận được dữ liệu
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    // lấy dữ liệu
                    String hoten= "" + ds.child("name").getValue();
                    String email= "" + ds.child("email").getValue();
                    String sdt= "" + ds.child("phone").getValue();

                    // đẩy dữ liệu lên
                    txtHotenid.setText(hoten);
                    txtGmailid.setText(email);
                    txtSDTid.setText(sdt);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // bắt sự kiện nút button edit thông tin người dùng
        ButtonEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiện hộp thoại chỉnh sửa thông tin người dùng: gồm đổi tên , đổi ảnh bìa
                // ảnh đại diện và sdt
                HopthoaiChinhsuaThongtinCaNhan();
            }
        });
        return view;
    }
    // hàm hộp thoại thay đổi thông tin người dùng
    private void HopthoaiChinhsuaThongtinCaNhan() {

        /* hiện hộp thoại chức năng chứa thông tin chỉnh sửa
         1.chỉnh sủa ảnh bìa
         2.chỉnh sửa ảnh đại diện
         3.chỉnh sửa tên
         4.chỉnh sửa số điện thoại*/

        // tạo đối tượng dialog (hộp thoại tùy chọn)có tên là HopThoaiTuyChon
        AlertDialog.Builder HopThoaiTuyChon= new AlertDialog.Builder(getActivity());
        // thiết lập tiêu đề cho hộp thoại
        HopThoaiTuyChon.setTitle("Thay đổi thông tin cá nhân");
        // các tùy chọn khi hiển thị hộp thoại
        // tạo một mảng chuỗi gồm:
        String TuyChon[]= {"Thay đổi ảnh bìa","Thay đổi ảnh nền","Thay đổi tên","Thay đổi số điện thoại"};
        // thiết lập các mục của hộp thoại, dạng single-choice list, gọi mảng Tùy chọn
        HopThoaiTuyChon.setItems(TuyChon, new DialogInterface.OnClickListener() {
            @Override
            // xử lý sự kiện khi chọn mục tương ứng (vd: thay đổi tên)
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){

                }
                else if(which==1){


                }
                else if(which==2){
                    // thay đổi tên
                   progressDialog.setMessage("Đang thay đổi tên...");
                    //gọi phương thức và truyền khóa "name" làm tham số để cập nhật nó là giá trị trong cơ sở dữ liệu
                    HienthiHopthoaiCapNhapTen("name");
                }
                else if(which==3){
                    // thay đổi số điện thoại
                    progressDialog.setMessage("Cập nhật số điện thoại");
                    HienthiHopthoaiCapNhapSDT("phone");
                }
            }
        });
        // tạo và hiển thị hộp thoại tùy chọn
        HopThoaiTuyChon.create().show();
    }
    private void HienthiHopthoaiCapNhapSDT(final String phone) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thay đổi số điện thoại người dùng"); // cập nhật tên hoặc sdt
        // set layout của hộp thoại thông báo
        LinearLayout linearLayout= new LinearLayout((getActivity()));
        linearLayout.setOrientation((linearLayout.VERTICAL));
        //canh lề 4x10
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        // Khởi tạo EditText Hint (mờ)
        final EditText editTextSDT = new EditText(getActivity());
        editTextSDT.setHint("Nhập số điện thoại"); // hint (dạng mờ để gợi ý người dùng)
        linearLayout.addView(editTextSDT);
        builder.setView(linearLayout);

        // thêm 2 button trong hộp thoại( đồ án thuộc sở hữu của Trần Đăng Khoa)
        // gồm: Thay đổi tên và thoát
        // button chọn thay đổi
        builder.setPositiveButton("Thay đổi số điện thoại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // láy giá trị của văn bản từ editTextTen
                final String valuesSDT= editTextSDT.getText().toString().trim();
                // kiểm tra người dùng đã nhập hay không !,  TextUtils.isEmpty => kiểm tra xem chuỗi đó có null hay không
                    progressDialog.show();
                    HashMap<String,Object> result= new HashMap<>();
                    result.put(phone,valuesSDT);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                // thay đổi tên và thoát tiến trình
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Số điện thoại đã thay đổi "+valuesSDT, Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // lỗi, hiện thông báo và thoát tiến trình
                            Toast.makeText(getActivity(), "Xảy ra lỗi !", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
        // button thoát
        builder.setNegativeButton("Thoát ", new DialogInterface.OnClickListener() {
            @Override
            // đóng hộp thoại khi ấn thoát
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // tạo và hiện thị hộp thoại thông báo
        builder.create().show();

    }
    private void HienthiHopthoaiCapNhapTen(final String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thay đổi tên người dùng"); // cập nhật tên hoặc sdt
        // set layout của hộp thoại thông báo
        LinearLayout linearLayout= new LinearLayout((getActivity()));
        linearLayout.setOrientation((linearLayout.VERTICAL));
        //canh lề 4x10
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        // Khởi tạo EditText Hint (mờ)
        final EditText editTextTen = new EditText(getActivity());
        editTextTen.setHint("Nhập tên"); // hint (dạng mờ để gợi ý người dùng)
        linearLayout.addView(editTextTen);
        builder.setView(linearLayout);

        // thêm 2 button trong hộp thoại( đồ án thuộc sở hữu của Trần Đăng Khoa)
        // gồm: Thay đổi tên và thoát
        // button chọn thay đổi
        builder.setPositiveButton("Thay đổi tên", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // láy giá trị của văn bản từ editTextTen
                final String valuesTen= editTextTen.getText().toString().trim();
                // kiểm tra người dùng đã nhập hay không !,  TextUtils.isEmpty => kiểm tra xem chuỗi đó có null hay không
                if(!TextUtils.isEmpty(valuesTen)){
                    progressDialog.show();
                    HashMap<String,Object> result= new HashMap<>();
                    result.put(key,valuesTen);

                   databaseReference.child(user.getUid()).updateChildren(result)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               // thay đổi tên và thoát tiến trình
                               public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                   Toast.makeText(getActivity(), "Đã đổi tên thành "+valuesTen, Toast.LENGTH_SHORT).show();

                               }
                           }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                          // lỗi, hiện thông báo và thoát tiến trình
                           Toast.makeText(getActivity(), "Xảy ra lỗi !", Toast.LENGTH_SHORT).show();
                       }
                   });

                }
                else{
                    Toast.makeText(getActivity(), "Tên không được để trống !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // button thoát
        builder.setNegativeButton("Thoát ", new DialogInterface.OnClickListener() {
            @Override
            // đóng hộp thoại khi ấn thoát
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // tạo và hiện thị hộp thoại thông báo
        builder.create().show();
    }

}

