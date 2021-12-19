package com.uts_pbp_d_9229.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;
import com.uts_pbp_d_9229.dao.City;
import com.uts_pbp_d_9229.R;
import com.uts_pbp_d_9229.databinding.ActivityRecyclerViewHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewHome extends RecyclerView.Adapter<RecyclerViewHome.MyViewHolder> {

    ActivityRecyclerViewHomeBinding binding;
    private Context context;
    private List<City> cityList, cityListFiltered;

    public RecyclerViewHome(){}

    public RecyclerViewHome(Context context, List<City> result){
        this.context = context;
        this.cityList = result;
        this.cityListFiltered = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_recycler_view_home, parent, false);
        final MyViewHolder holder = new MyViewHolder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final City city = cityListFiltered.get(position);

        holder.binding.setCity(city);
    }

    @Override
    public int getItemCount() {
        return (cityListFiltered != null) ? cityListFiltered.size() : 0; }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ActivityRecyclerViewHomeBinding binding;

        public MyViewHolder(@NonNull ActivityRecyclerViewHomeBinding bind){
            super(bind.getRoot());
            this.binding = bind;
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString();
                if (userInput.isEmpty()) {
                    cityListFiltered = cityList;
                }
                else {
                    List<City> filteredList = new ArrayList<>();
                    for(City city : cityList) {
                        if(String.valueOf(city.getNama()).toLowerCase().contains(userInput)) {
                            filteredList.add(city);
                        }
                    }
                    cityListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = cityListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cityListFiltered = (ArrayList<City>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}