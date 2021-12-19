package com.uts_pbp_d_9229.view;

import static com.android.volley.Request.Method.GET;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.uts_pbp_d_9229.adapter.RecyclerViewHome;
import com.uts_pbp_d_9229.api.CityAPI;
import com.uts_pbp_d_9229.dao.City;
import com.uts_pbp_d_9229.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment<RecyclerViewAdapter> extends Fragment {

    private ArrayList<City> cityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewHome adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextInputEditText searchView;
    private SharedPreferences preference;
    private TextView tvWelcome;
    private View view;

    public HomeFragment(){}

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        setAtribut();
        getCities();

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setAtribut() {
        searchView = view.findViewById(R.id.searchview);
        tvWelcome  = view.findViewById(R.id.tv_welcome);
        preference = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        tvWelcome.setText("Welcome, "+ preference.getString("name", null).substring(0,5));

        //recycler view
        recyclerView = view.findViewById(R.id.recyclerview_city);
        adapter = new RecyclerViewHome(view.getContext(), cityList);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    public void getCities() {
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CityAPI.URL_GET_ALL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!cityList.isEmpty())
                        cityList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id          = Integer.parseInt(jsonObject.optString("id"));
                        String name     = jsonObject.optString("name");
                        String picture  = jsonObject.optString("picture");

                        cityList.add(new City(id, name, picture));
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(stringRequest);
    }

    public void callParentMethod(){
        getActivity().onBackPressed();
    }
}
