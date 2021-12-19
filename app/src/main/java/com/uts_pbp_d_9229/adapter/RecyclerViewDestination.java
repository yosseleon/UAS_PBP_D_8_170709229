package com.uts_pbp_d_9229.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.uts_pbp_d_9229.R;
import com.uts_pbp_d_9229.dao.Destination;
import com.uts_pbp_d_9229.databinding.ActivityRecyclerViewCategoryBinding;
import com.uts_pbp_d_9229.databinding.ActivityRecyclerViewDestinationBinding;
import com.uts_pbp_d_9229.view.DetailDestinationFragment;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewDestination extends RecyclerView.Adapter<RecyclerViewDestination.MyViewHolder> {

    ActivityRecyclerViewDestinationBinding binding;
    private Context context;
    private List<Destination> destinations, destinationsFiltered;

    public RecyclerViewDestination(){}

    public RecyclerViewDestination(Context context, List<Destination> result){
        this.context = context;
        this.destinations = result;
        this.destinationsFiltered = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_recycler_view_destination, parent, false);
        final MyViewHolder holder = new MyViewHolder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Destination destination = destinationsFiltered.get(position);

        CardView cardView = holder.itemView.findViewById(R.id.ParentAdapter);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putSerializable("destination", destination);
                DetailDestinationFragment detailDestination = new DetailDestinationFragment();
                detailDestination.setArguments(data);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment , detailDestination)
                        .addToBackStack( "tag" )
                        .commit();
            }
        });

        holder.binding.setDestination(destination);
    }

    @Override
    public int getItemCount() {
        return (destinationsFiltered != null) ? destinationsFiltered.size() : 0; }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ActivityRecyclerViewDestinationBinding binding;

        public MyViewHolder(@NonNull ActivityRecyclerViewDestinationBinding bind){
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
                    destinationsFiltered = destinations;
                }
                else {
                    List<Destination> filteredList = new ArrayList<>();
                    for(Destination destination : destinations) {
                        if(String.valueOf(destination.getName()).toLowerCase().contains(userInput)
                        || String.valueOf(destination.getCity_name()).toLowerCase().contains(userInput)) {
                            filteredList.add(destination);
                        }
                    }
                    destinationsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = destinationsFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                destinationsFiltered = (ArrayList<Destination>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Filter getFilterByCategory() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString().toLowerCase();
                if (userInput.isEmpty()) {
                    destinationsFiltered = destinations;
                }
                else {
                    List<Destination> filteredList = new ArrayList<>();
                    for(Destination destination : destinations) {
                        if(String.valueOf(destination.getCategory()).toLowerCase().contains(userInput)) {
                            filteredList.add(destination);
                        }
                    }
                    destinationsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = destinationsFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                destinationsFiltered = (ArrayList<Destination>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}