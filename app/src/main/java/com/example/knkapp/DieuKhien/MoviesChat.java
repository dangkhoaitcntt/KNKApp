package com.example.knkapp.DieuKhien;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knkapp.Models.ModelChat;
import com.example.knkapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
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
    public void onBindViewHolder(@NonNull MyHoder holder, int position) {
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
        // view holder class
        public MyHoder(@NonNull View itemView) {
            super(itemView);
            tinnhan = itemView.findViewById(R.id.txtMessage_id);
            daxem= itemView.findViewById(R.id.txtdaXem_id);
            thoigian= itemView.findViewById(R.id.txtTime_id);
        }
    }

}
