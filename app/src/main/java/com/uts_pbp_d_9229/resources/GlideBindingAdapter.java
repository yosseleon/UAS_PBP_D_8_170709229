package com.uts_pbp_d_9229.resources;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uts_pbp_d_9229.R;

public class GlideBindingAdapter {
    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView view, int imageUrl){

        Context context = view.getContext();

        RequestOptions option = new RequestOptions()
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24);

        Glide.with(context)
                .setDefaultRequestOptions(option)
                .load(imageUrl)
                .into(view);
    }

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView view, String imageUrl){

        Context context = view.getContext();

        RequestOptions option = new RequestOptions()
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24);

        Glide.with(context)
                .setDefaultRequestOptions(option)
                .load(imageUrl)
                .into(view);
    }
}
