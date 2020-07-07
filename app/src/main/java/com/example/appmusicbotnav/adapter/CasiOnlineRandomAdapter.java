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
import com.example.appmusicbotnav.modelOnline.Casi;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CasiOnlineRandomAdapter extends RecyclerView.Adapter<CasiOnlineRandomAdapter.ViewHoder> {

    private Context context;
    private ArrayList<Casi> casiArrayList;
    private Fragment fragment;
    private ArrayList<Integer> listicon = new ArrayList<>();

    public CasiOnlineRandomAdapter(Context context, ArrayList<Casi> casiArrayList, Fragment fragment){
        this.context = context;
        if(casiArrayList == null || casiArrayList.isEmpty())
            this.casiArrayList = new ArrayList<>();
        else
        this.casiArrayList = casiArrayList;
        this.fragment = fragment;
        listicon.add(R.drawable.ic_casi_1);
        listicon.add(R.drawable.ic_casi_2);
        listicon.add(R.drawable.ic_casi_4);
    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_casi_online_random, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        Casi casi = casiArrayList.get(position);
        holder.tv_tencasi.setText("Tên ca sĩ: "  + casi.getTenCaSi());
        Picasso.with(context).load(listicon.get(position)).into(holder.iv_casi);
    }

    @Override
    public int getItemCount() {
        return casiArrayList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder{
        private ImageView iv_casi;
        private TextView tv_tencasi;

        public ViewHoder(@NonNull final View itemView) {
            super(itemView);
            iv_casi = itemView.findViewById(R.id.iv_casi_online_random);
            tv_tencasi = itemView.findViewById(R.id.tv_casi_online_random_tencasi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Casi casi = casiArrayList.get(getPosition());
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) casi.getListBaiHat());
                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.action_item_online_to_danhsachbaihatOnline, bundle);
                }
            });
        }
    }
}
