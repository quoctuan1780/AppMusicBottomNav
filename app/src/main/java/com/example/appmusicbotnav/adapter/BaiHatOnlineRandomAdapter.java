package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.modelOnline.Baihat;
import java.util.ArrayList;

public class BaiHatOnlineRandomAdapter extends RecyclerView.Adapter<BaiHatOnlineRandomAdapter.ViewHoder> {
    private Context context;
    private ArrayList<Baihat> baihatArrayList;

    public BaiHatOnlineRandomAdapter(Context context, ArrayList<Baihat> albumarraylist) {
        this.context = context;
        this.baihatArrayList = albumarraylist;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_baihat, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        Baihat baihat = baihatArrayList.get(position);
        holder.tv_tenbaihat.setText(baihat.getTenBaiHat());
        holder.tv_tencasi.setText(baihat.getTenTacGia());
    }

    @Override
    public int getItemCount() {
        return baihatArrayList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder{
        private TextView tv_tenbaihat, tv_tencasi;

        public ViewHoder(@NonNull final View itemView) {
            super(itemView);
            tv_tenbaihat = itemView.findViewById(R.id.tv_music_name);
            tv_tencasi = itemView.findViewById(R.id.tv_music_subtitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent phatnhac = new Intent(context, PhatNhac.class);
                    phatnhac.putExtra("vitri", getPosition());
                    phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) baihatArrayList);
                    try {
                        context.startActivity(phatnhac);
                    }catch (Exception e){

                    }
                }
            });
        }
    }
}
