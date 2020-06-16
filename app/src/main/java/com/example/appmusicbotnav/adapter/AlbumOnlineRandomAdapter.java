package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.fragment.Online;
import com.example.appmusicbotnav.modelOnline.Album;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AlbumOnlineRandomAdapter extends RecyclerView.Adapter<AlbumOnlineRandomAdapter.ViewHoder> {

    private Context context;
    private ArrayList<Album> albumarraylist;
    private Fragment fragment;

    public AlbumOnlineRandomAdapter(Context context, ArrayList<Album> albumarraylist, Fragment fragment) {
        this.context = context;
        this.albumarraylist = albumarraylist;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_album_online_random, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        Album album = albumarraylist.get(position);
        holder.tv_tenalbum.setText("Tên album: " + album.getTenAlbum());
        holder.tv_soluongbai.setText("Số bài: " + album.getSoLuongBai() + "");
        Picasso.with(context).load(R.drawable.ic_album).into(holder.iv_album);
    }

    @Override
    public int getItemCount() {
        return albumarraylist.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder{
        private ImageView iv_album;
        private TextView tv_tenalbum, tv_soluongbai;

        public ViewHoder(@NonNull final View itemView) {
            super(itemView);
            iv_album = itemView.findViewById(R.id.iv_anbum_online_random);
            tv_tenalbum = itemView.findViewById(R.id.tv_album_online_random_tenalbum);
            tv_soluongbai = itemView.findViewById(R.id.tv_album_online_random_slbai);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Album albumhientai = albumarraylist.get(getPosition());
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) albumhientai.getListBaiHat());
                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.action_item_online_to_danhsachbaihatOnline, bundle);
                }
            });
        }
    }
}
