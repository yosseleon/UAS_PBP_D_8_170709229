package com.uts_pbp_d_9229.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uts_pbp_d_9229.R;
import com.uts_pbp_d_9229.dao.Destination;


public class DetailDestinationFragment extends Fragment {
    private View view;
    private Destination destination;
    private TextView tvNama, tvLocation, tvDescription;
    private ImageView ivPicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_destination, container, false);

        init();
        return view;
    }

    private void init() {
        destination   = (Destination) getArguments().getSerializable("destination");
        tvNama              = view.findViewById(R.id.destination_name);
        tvLocation          = view.findViewById(R.id.destination_location);
        tvDescription       = view.findViewById(R.id.destination_description);
        ivPicture           = view.findViewById(R.id.iv_picture);

        tvNama.setText(destination.getName());
        tvDescription.setText(destination.getDescription());
        tvLocation.setText(destination.getLocation());
        Glide.with(view.getContext())
                .load(destination.getPicture())
                .into(ivPicture);
    }
}