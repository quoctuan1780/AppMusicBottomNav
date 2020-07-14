package com.example.appmusicbotnav.modelYoutube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThumbnailYT {
    @SerializedName("medium")
    @Expose
    private MediumThumb mediumThumb;

    public ThumbnailYT(MediumThumb mediumThumb) {
        this.mediumThumb = mediumThumb;
    }

    public ThumbnailYT() {
    }

    public MediumThumb getMediumThumb() {
        return mediumThumb;
    }

    public void setMediumThumb(MediumThumb mediumThumb) {
        this.mediumThumb = mediumThumb;
    }

    public class MediumThumb {
        @SerializedName("url")
        @Expose
        private String url;

        public MediumThumb(String url) {
            this.url = url;
        }

        public MediumThumb() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
