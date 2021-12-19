package com.uts_pbp_d_9229.view;

import static com.android.volley.Request.Method.GET;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.uts_pbp_d_9229.R;
import com.uts_pbp_d_9229.adapter.RecyclerViewCategory;
import com.uts_pbp_d_9229.adapter.RecyclerViewDestination;
import com.uts_pbp_d_9229.api.CategoryAPI;
import com.uts_pbp_d_9229.api.DestinationAPI;
import com.uts_pbp_d_9229.dao.Category;
import com.uts_pbp_d_9229.dao.Destination;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Destination> destinations = new ArrayList<>();
    private RecyclerView recyclerViewDestination, recyclerViewCategory;
    private RecyclerViewDestination adapterDestination;
    private RecyclerViewCategory adapterCategory;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextInputEditText searchView;
    private String selectedCategory = "Beach";
    private View view;

    public CategoryFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);

        searchView   = view.findViewById(R.id.searchview);

        setAdapterCategory();
        setAdapterDestination();
        getCategories();
        getDestinations();

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapterDestination.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    public  void setAdapterDestination(){
        recyclerViewDestination = view.findViewById(R.id.recyclerview_destination);
        adapterDestination = new RecyclerViewDestination(view.getContext(), destinations);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewDestination.setLayoutManager(mLayoutManager);
        recyclerViewDestination.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDestination.setAdapter(adapterDestination);
    }

    public void setAdapterCategory() {
        recyclerViewCategory = view.findViewById(R.id.recyclerview_category);
        adapterCategory = new RecyclerViewCategory(view.getContext(), categories, selectedCategory,
                new RecyclerViewCategory.OnItemClickListener() {
                    @Override
                    public void onItemClick(Category category) {
                        selectedCategory = category.getName();
                        adapterCategory.notifyDataSetChanged();
                        adapterDestination.getFilterByCategory().filter(category.getName());
                    }
                });
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(view.getContext(),
                        LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(linearLayoutManager);
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCategory.setAdapter(adapterCategory);
    }

    public void getDestinations() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, DestinationAPI.URL_GET_ALL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!destinations.isEmpty())
                        destinations.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id              = Integer.parseInt(jsonObject.optString("id"));
                        String city_name    = jsonObject.optString("city_name");
                        String category     = jsonObject.optString("category_name");
                        String name         = jsonObject.optString("name");
                        String picture      = jsonObject.optString("picture");
                        String location     = jsonObject.optString("location");
                        String description  = jsonObject.optString("details");

                        destinations.add(new Destination(id, city_name, name, category, picture,
                                location, description));
                    }
                    adapterDestination.notifyDataSetChanged();
                    adapterDestination.getFilterByCategory().filter(selectedCategory);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(stringRequest);
    }

    public void getCategories() {
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CategoryAPI.URL_GET_ALL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!categories.isEmpty())
                        categories.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id          = Integer.parseInt(jsonObject.optString("id"));
                        String name     = jsonObject.optString("name");

                        categories.add(new Category(id, name));
                    }
                    adapterCategory.notifyDataSetChanged();
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