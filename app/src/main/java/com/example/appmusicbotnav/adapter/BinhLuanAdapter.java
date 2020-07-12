package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.modelOnline.Comment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BinhLuanAdapter extends RecyclerView.Adapter<BinhLuanAdapter.BinhLuanViewHolder> {

    private Context mContext;
    private ArrayList<Comment> mData;

    public BinhLuanAdapter(Context mContext, ArrayList<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public BinhLuanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_comment,parent,false);
        return new BinhLuanViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull BinhLuanViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.userphoto);
        holder.tv_name.setText(mData.get(position).getIdNguoiDung()+"");
        holder.tv_content.setText(mData.get(position).getNoiDung());
        holder.tv_date.setText(chuyendoingay(mData.get(position).getThoiDiemBinhLuan()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class BinhLuanViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tv_name,tv_content,tv_date;

        public BinhLuanViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.comment_user_img);
            tv_name = itemView.findViewById(R.id.comment_username);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_date = itemView.findViewById(R.id.comment_date);
        }
    }

    private String chuyendoingay(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            java.util.Date dutyDay = (java.util.Date) simpleDateFormat.parse(date);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            return df.format("dd-MM-yyyy", dutyDay).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAdapter(Comment comment){
        mData.add(comment);
        notifyDataSetChanged();
    }
}
