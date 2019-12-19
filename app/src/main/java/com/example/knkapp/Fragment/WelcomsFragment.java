package com.example.knkapp.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.knkapp.MainActivity;
import com.example.knkapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomsFragment extends Fragment {

    // khai báo lưu trữ fireBase
    FirebaseAuth firebaseAuth;


    public WelcomsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_trangchu, container, false);
        return view;
    }


    // tìm kiếm bạn bè
    private void checkUserStatus(){
        // lấy người dùng hiện tại
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user != null){
            // người dùng đã đăng nhâp ở đây
            // lấy email đăng nhập của người dùng
            //txtHoso.setText(user.getEmail());
        } else {
            // người dùng chưa đăng nhập, đi đến main activity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);// để hiển thị menu tùy chọn
        super.onCreate(savedInstanceState);
    }

    /*tùy chon menu*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // menu nâng cao dùng infale
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu,inflater);
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
