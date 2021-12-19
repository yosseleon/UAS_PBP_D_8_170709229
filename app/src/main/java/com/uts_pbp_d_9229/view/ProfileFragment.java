package com.uts_pbp_d_9229.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uts_pbp_d_9229.LoginActivity;
import com.uts_pbp_d_9229.R;

public class ProfileFragment extends Fragment {

    private TextView tvName, tVEmail, tvAddress, tvPhone, tvLogout;
    private Button btnEdit;
    private ImageView ivProfile;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName      = view.findViewById(R.id.tv_name);
        tVEmail     = view.findViewById(R.id.tv_email);
        tvAddress   = view.findViewById(R.id.tv_address);
        tvPhone     = view.findViewById(R.id.tv_noHp);
        tvLogout    = view.findViewById(R.id.tv_logout);
        ivProfile   = view.findViewById(R.id.profile_image);
        btnEdit     = view.findViewById(R.id.btn_edit_profile);

        preference = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        editor = preference.edit();
        tvName.setText(preference.getString("name", null));
        tVEmail.setText(preference.getString("email", null));
        tvAddress.setText(preference.getString("address", null));
        tvPhone.setText(preference.getString("nohp", null));
        Glide.with(view.getContext())
                .load(preference.getString("picture", null))
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivProfile);

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("isLogin", "tidak");
                editor.putString("userId", "");
                editor.putString("name", "");
                editor.putString("email", "");
                editor.putString("address", "");
                editor.putString("nohp", "");
                editor.putString("picture", "");
                editor.apply();

                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), EditProfile.class));
            }
        });

        return view;
    }

    public void callParentMethod(){
        getActivity().onBackPressed();
    }
}