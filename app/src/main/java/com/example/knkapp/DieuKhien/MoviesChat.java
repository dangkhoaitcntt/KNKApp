package com.example.knkapp.DieuKhien;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knkapp.Models.ModelChat;
import com.example.knkapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MoviesChat extends RecyclerView.Adapter<MoviesChat.MyHoder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> DSnhanTin;
    FirebaseUser firebaseUser;

    public MoviesChat(Context context, List<ModelChat> DSnhanTin) {
        this.context = context;
        this.DSnhanTin = DSnhanTin;
    }
    @NonNull
    @Override
    public MyHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType== MSG_TYPE_LEFT){
            View view= LayoutInflater.from(context)
                    .inflate(R.layout.row_chat_left,parent,false);
            return new MyHoder(view);
        }
        else
            {
            View view= LayoutInflater.from(context)
                    .inflate(R.layout.row_chat_right,parent,false);
            return new MyHoder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull MyHoder holder, final int position) {
        // lấy dữ liệu
        String tinNhan= DSnhanTin.get(position).getTinnhan();
        String thoiGian= DSnhanTin.get(position).getThoigian();

        // chuyển đổi thoi gian sang dd/mm/yyyy am=pm
        Calendar calendar= Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(thoiGian));
        String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        // lấy dữ liệu từ firebase
        holder.tinnhan.setText(tinNhan);
        holder.thoigian.setText(dateTime);


        // ------code xóa tin nhắn ---------
        // chọn để show thông báo xóa tin nhắn
        holder.tinnhanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiện màng hình thông báo
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                // tiêu đề thông báo
                builder.setTitle("Xóa tin nhắn");
                // nội dung thông báo
                builder.setMessage("Bạn có muốn xóa tin nhắn này ?");
                // button xóa tin nhắn
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // gọi hàm xóa tin nhắn
                        xoaTinNhan(position);
                    }
                });
                // button thoát
                builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // thoát màng hình thông báo
                        dialog.dismiss();
                    }
                });
                // tạo và hiển thị thông báo
                builder.create().show();
            }
        });
        // thiết lập tình trạng xem/ đã gửi tin nhắn
        if(position==DSnhanTin.size()-1){
            if(DSnhanTin.get(position).isDaxem()){
                holder.daxem.setText("Đã xem");
            }
            else{
                holder.daxem.setText("Đã gửi");
            }
        }else {
            holder.daxem.setVisibility(View.GONE);
        }
    }

    private void xoaTinNhan(int position) {

        // lấy chủ sở hữu
        final String myUid= FirebaseAuth.getInstance().getUid();

        // lấy thời gian của tin nhắn
        //so sánh dấu thời gian của tin nhắn nhấp chuột với tất cả tin nhắn
        // trong các cuộc trò chuyện
        //trong đó cả hai giá trị khớp sẽ xóa thông báo đó

        String dauThoiGian= DSnhanTin.get(position).getThoigian();
        final DatabaseReference databaseReference= FirebaseDatabase
                .getInstance().getReference("Chats");
        Query query= databaseReference.orderByChild("thoigian").equalTo(dauThoiGian);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    // để người gửi tin chỉ xóa được tin nhắn của mình
                    // nếu khớp thì xóa tin nhắn
                    if(ds.child("guitin").getValue().equals(myUid)){
                        //2 kieu xoa
                        //xóa tin nhắn khỏi cuộc trò chuyện
                        ds.getRef().removeValue();
                        // đặt giá trị tin nhắn "tin nhắn đã xóa"
                       /* HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("tinnhan","tin nhắn này đã xóa...");
                        ds.getRef().updateChildren(hashMap);*/

                        Toast.makeText(context, "Xóa tin nhắn thành công !", Toast.LENGTH_SHORT).show();
                        
                    }
                    else{
                        Toast.makeText(context, "Xóa tin nhắn thất bại !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return DSnhanTin.size();
    }

    @Override
    public int getItemViewType(int position) {
        // hiển thị người dùng đang đăng nhập
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(DSnhanTin.get(position).getGuitin().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    class  MyHoder extends RecyclerView.ViewHolder{
        TextView tinnhan, thoigian, daxem;
        LinearLayout tinnhanLayout; // khi click vào tin nhắn thì hiển thị thông báo xóa tin nhắn
        // view holder class
        public MyHoder(@NonNull View itemView) {
            super(itemView);
            tinnhan = itemView.findViewById(R.id.txtMessage_id);
            daxem= itemView.findViewById(R.id.txtdaXem_id);
            thoigian= itemView.findViewById(R.id.txtTime_id);
            tinnhanLayout= itemView.findViewById(R.id.tinnhanLayout_id);
        }
    }

}
