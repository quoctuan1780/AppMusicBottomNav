package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.activity.Youtube;
import com.example.appmusicbotnav.modelYoutube.ModelHome;
import com.example.appmusicbotnav.modelYoutube.VideoYT;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.List;


public class YoutubeVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<VideoYT> videoList;
    private ModelHome modelHome;
    public YoutubeVideoAdapter() {
    }

    public YoutubeVideoAdapter(Context context, List<VideoYT> videoList, ModelHome modelHome) {
        this.context = context;
        this.videoList = videoList;
        this.modelHome = modelHome;
    }

    class YoutubeHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView judul, tanggal;

        public YoutubeHolder(@NonNull final View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.imageView);
            judul = itemView.findViewById(R.id.tv_judul);
            tanggal = itemView.findViewById(R.id.tv_tgUpdate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(PhatNhac.choinhac != null && PhatNhac.choinhac.isPlaying()){
                        PhatNhac.choinhac.pause();
                    }
                    Intent intent = new Intent(context, Youtube.class);
                    intent.putExtra("videoId", videoList.get(getPosition()).getId().getVideoId());
                    Gson gson = new Gson();
                    String json = gson.toJson(modelHome);
                    intent.putExtra("videoList", json);
                    context.startActivity(intent);
                }
            });
        }

        public void setData(VideoYT data) {
            String getJudul = data.getSnippet().getTitle();
            String getTgl = data.getSnippet().getPublishedAt();
            String getThumb = data.getSnippet().getThumbnails().getMediumThumb().getUrl();
            judul.setText(getJudul);
            tanggal.setText(getTgl);
            Picasso.with(context).load(getThumb).into(thumbnail);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_row_video,parent,false);
        return new YoutubeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoYT videoYT = videoList.get(position);
        YoutubeHolder yth= (YoutubeHolder) holder;
        yth.setData(videoYT);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
