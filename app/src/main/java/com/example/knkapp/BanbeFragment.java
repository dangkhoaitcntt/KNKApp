package com.example.knkapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.knkapp.Models.ModelBanBe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BanbeFragment extends Fragment {

    // khai báo sử dụng RecyclerView.
    RecyclerView recyclerView;
    MoviesBanbe moviesBanbe;
    List<ModelBanBe> modelBanBeList;

    // người dùng
    FirebaseAuth firebaseAuth;

    public BanbeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_banbe, container, false);



        // lấy người dùng
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.hienthibanbe_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        modelBanBeList = new ArrayList<>();
        
        LayTatCaBanBe();

        return view;
    }

    private void LayTatCaBanBe() {
        // lấy bạn bè
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        // lấy đường dẫn tên "users" chứa thông tin bạn bè
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelBanBeList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelBanBe modelBanBe = ds.getValue(ModelBanBe.class);

                    if(!modelBanBe.getUid().equals(firebaseUser.getUid())){
                        modelBanBeList.add(modelBanBe);
                    }
                }
                moviesBanbe = new MoviesBanbe(getActivity(), modelBanBeList);
                recyclerView.setAdapter(moviesBanbe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void TimKiemBanBan(final String query) {
        // lấy bạn bè
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        // lấy đường dẫn tên "users" chứa thông tin bạn bè
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelBanBeList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelBanBe modelBanBe = ds.getValue(ModelBanBe.class);

                    if(!modelBanBe.getUid().equals(firebaseUser.getUid())){
                        if(modelBanBe.getName().toLowerCase().contains(query.toLowerCase())||
                        modelBanBe.getEmail().toLowerCase().contains(query)){
                            modelBanBeList.add(modelBanBe);
                        }

                        //modelBanBeList.add(modelBanBe);
                    }
                }
                moviesBanbe = new MoviesBanbe(getActivity(), modelBanBeList);

                moviesBanbe.notifyDataSetChanged();

                recyclerView.setAdapter(moviesBanbe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void KiemtraTinhtrangBanbe(){
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
         // tìm kiếm
        MenuItem item= menu.findItem(R.id.action_timkiemBanBe);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);


        // tìm kiếm danh sách
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // gọi khi người dùng nhấn button tìm kiếm từ bàn phím
                if(!TextUtils.isEmpty(query.trim())){
                    // tìm ký tự
                    TimKiemBanBan(query);

                }else{
                    LayTatCaBanBe();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // gọi khi người dùng nhấn button tìm kiếm từ bàn phím
                if(!TextUtils.isEmpty(newText.trim())){
                    // tìm ký tự
                    TimKiemBanBan(newText);

                }else{
                    LayTatCaBanBe();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    // xử lý khi click vào mục của menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // lấy id của các mục
        int id= item.getItemId();
        if(id== R.id.action_dangxuat){
            firebaseAuth.signOut();// đăng xuất ra khổi tài khoản
            KiemtraTinhtrangBanbe();
        }
        return super.onOptionsItemSelected(item);
    }

}


