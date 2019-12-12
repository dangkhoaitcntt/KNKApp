package com.example.knkapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.PipedInputStream;


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
    ImageView ImAvataid;
    TextView txtHotenid, txtGmailid, txtSDTid;


    public HosoFragment() {
        // Required empty public constructor
    }


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
        ImAvataid= view.findViewById(R.id.Avata_id);
        txtHotenid= view.findViewById(R.id.txt_HShoten_id);
        txtGmailid= view.findViewById(R.id.txt_HSgmail_id);
        txtSDTid= view.findViewById(R.id.txt_HSsdt_id);

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
                    String HinhanhAvata= "" + ds.child("image").getValue();

                    // đẩy dữ liệu lên
                    txtHotenid.setText(hoten);
                    txtGmailid.setText(email);
                    txtSDTid.setText(sdt);
                    try{
                        // nếu nhận được hình ảnh thì thiết lập avata
                        Picasso.get().load(HinhanhAvata).into(ImAvataid);
                    }
                    catch (Exception e){
                        // nếu không thì lấy ảnh mặc định
                        Picasso.get().load(R.drawable.ic_add_image).into(ImAvataid);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
