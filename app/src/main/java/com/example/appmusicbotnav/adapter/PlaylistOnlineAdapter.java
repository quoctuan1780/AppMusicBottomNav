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
import com.example.appmusicbotnav.model.Slide;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.modelOnline.Playlist;
import com.example.appmusicbotnav.session.Session;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class PlaylistOnlineAdapter extends RecyclerView.Adapter<PlaylistOnlineAdapter.ViewHoder> {

    private Context context;
    private ArrayList<Playlist> playlistArrayList;
    private Fragment fragment;
    private ArrayList<Slide> anhArrayList = new ArrayList<>();
    private Session session;

    public PlaylistOnlineAdapter(Context context, ArrayList<Playlist> playlistArrayList, Fragment fragment, Session session) {
        this.context = context;
        this.playlistArrayList = playlistArrayList;
        this.fragment = fragment;
        this.session = session;
        anhArrayList.add(new Slide(R.drawable.playlist1));
        anhArrayList.add(new Slide(R.drawable.playlist2));
        anhArrayList.add(new Slide(R.drawable.playlist3));
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_playlist_online_custom, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        if(!session.getToken().equals("")) {
            Playlist playlist = playlistArrayList.get(position);
            holder.tv_ten_playlist.setText(playlist.getTenPlayList());
            Picasso.with(context).load(anhArrayList.get(position).getHinh()).into(holder.iv_anh_playlist);
        }else{
            Picasso.with(context).load(R.drawable.nenxam).into(holder.iv_anh_playlist);
            holder.tv_ten_playlist.setText("Bạn chưa đăng nhập");
        }
    }

    @Override
    public int getItemCount() {
        return playlistArrayList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder{
        private ImageView iv_anh_playlist;
        private TextView tv_ten_playlist;


        public ViewHoder(@NonNull final View itemView) {
            super(itemView);
            iv_anh_playlist = (ImageView) itemView.findViewById(R.id.iv_playlist_online);
            tv_ten_playlist = (TextView) itemView.findViewById(R.id.tv_ten_playlist_online);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Baihat> baiHatArrayList = new ArrayList<>();
                    for(int i = 0; i < playlistArrayList.get(getPosition()).getListBaiHatPlayList().size(); i++){
                        baiHatArrayList.add(playlistArrayList.get(getPosition()).getListBaiHatPlayList().get(i).getBaiHat());
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) baiHatArrayList);
                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.action_item_online_to_danhsachbaihatOnline, bundle);
                }
            });
        }
    }
}
